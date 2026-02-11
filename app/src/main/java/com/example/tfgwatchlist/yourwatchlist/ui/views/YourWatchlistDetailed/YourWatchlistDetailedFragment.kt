package com.example.tfgwatchlist.yourwatchlist.ui.views.YourWatchlistDetailed

import android.app.Dialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.tfgwatchlist.R
import com.example.tfgwatchlist.core.datastore.AuthDataStore
import com.example.tfgwatchlist.core.utils.generarURL
import com.example.tfgwatchlist.core.utils.generarURLYoutube
import com.example.tfgwatchlist.databinding.DialogTaskChangestatefrommediaBinding
import com.example.tfgwatchlist.databinding.DialogTaskDeletefromyourwatchlistBinding
import com.example.tfgwatchlist.databinding.DialogTaskDeletionconfirmedBinding
import com.example.tfgwatchlist.databinding.FragmentYourWatchlistDetailedBinding
import com.example.tfgwatchlist.watchlist.ui.views.watchlistDetailed.WatchlistDetailedFragmentDirections
import com.example.tfgwatchlist.yourwatchlist.data.network.model.ItemMediaMongoItem
import com.example.tfgwatchlist.yourwatchlist.data.network.model.MovieMongoItem
import com.example.tfgwatchlist.yourwatchlist.data.network.model.SerieMongoItem
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class YourWatchlistDetailedFragment : Fragment() {

    private lateinit var binding: FragmentYourWatchlistDetailedBinding
    private lateinit var adapterReparto: YourWatchlistDetailedAdapter
    private lateinit var nombreUsuario: String
    //Pendiente esto
    private val viewModel by viewModels<YourWatchlistDetailedViewModel> {
        YourWatchlistDetailedViewModel.Factory
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentYourWatchlistDetailedBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val authDataStore = AuthDataStore(requireContext())
        lifecycleScope.launch {
            authDataStore.userToken.collect { storedUserName ->
                storedUserName?.let{
                    nombreUsuario = storedUserName
                    val media = arguments?.getSerializable("media") as ItemMediaMongoItem
                    //nombreUsuario = arguments?.getSerializable("userName") as String
                    adapterReparto = YourWatchlistDetailedAdapter()
                    binding.rvYourWatchlistDetailsCast.layoutManager = LinearLayoutManager(requireContext())
                    binding.rvYourWatchlistDetailsCast.adapter = adapterReparto
                    initUI(media);
                    observeUI();
                }

            }
        }
        /*val media = arguments?.getSerializable("media") as ItemMediaMongoItem
        //nombreUsuario = arguments?.getSerializable("userName") as String
        adapterReparto = YourWatchlistDetailedAdapter()
        binding.rvYourWatchlistDetailsCast.layoutManager = LinearLayoutManager(requireContext())
        binding.rvYourWatchlistDetailsCast.adapter = adapterReparto
        initUI(media);
        observeUI();
        */
    }
    private fun initUI(itemMedia: ItemMediaMongoItem){
        when(itemMedia){
            is MovieMongoItem -> {
                Log.i("Your watchlist detailed", "Es una película")
            }
            is SerieMongoItem -> {
                Log.i("Your watchlist detailed", "Es una serie")
            }
        }
        rellenarData(itemMedia)
        setSpinnerMedia(itemMedia)
    }

    private fun observeUI(){
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED){
                viewModel.uiState.collectLatest { uiState ->
                    when(uiState){
                        YourWatchlistDetailedUiState.Empty -> {
                            Log.i("ObserveUI", "Empty")
                        }
                        YourWatchlistDetailedUiState.Loading -> {
                            Log.i("ObserveUI", "Loading")
                            binding.pbCastYourWatchlist.isVisible = true
                        }
                        is YourWatchlistDetailedUiState.Error -> {
                            Log.i("Chando", uiState.message)
                            binding.tvInfoCastYourWatchlist.text = uiState.message
                            binding.pbCastYourWatchlist.isVisible = false
                        }
                        is YourWatchlistDetailedUiState.Success -> {
                            Log.i("Chando", "${uiState.casts}")
                            binding.rvYourWatchlistDetailsCast.isVisible = true;
                            binding.tvInfoCastYourWatchlist.text = ""
                            binding.pbCastYourWatchlist.isVisible = false
                            adapterReparto.submitList(uiState.casts)
                        }
                    }
                }
            }
        }
    }

    private fun rellenarData(mediaItem: ItemMediaMongoItem){
        binding.tvYourWatchlistDetailsTituloMedia.text = mediaItem.nombre
        binding.tvYourWatchlistDetailsPremisaMedia.text = mediaItem.premisa

        val generos = mediaItem.generos.map{it.nombre}

        binding.tvYourWatchlistDetailsGenres.text = generos.toString()

        Glide.with(binding.root.context)
            .load(generarURL("w780${mediaItem.imagen}"))
            .into(binding.ivYourWatchlistDetailsImagenPoster)

        Glide.with(binding.root.context)
            .load(generarURL("original${mediaItem.banner}"))
            .into(binding.ivYourWatchlistDetailsImagenBanner)

        binding.tvYourWatchlistDetailsState.text = getString(R.string.statusText) + mediaItem.estado

        if(mediaItem.trailer != null){
            binding.imgBtnVerTrailer.setOnClickListener {
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(generarURLYoutube("${mediaItem.trailer}")))
                context?.startActivity(intent)
            }

            Glide.with(binding.root.context)
                .load(generarURLYoutube("${mediaItem.trailer}"))
                .into(binding.imgBtnVerTrailer)
            //Pendiente poner acá lo de la generación de la imagen en el boton
        } else {
            binding.imgBtnVerTrailer.isVisible = false
        }

        when(mediaItem){
            is MovieMongoItem -> {
                binding.btnMostrarTemporadas.isVisible = false
                binding.tvGeneralInfoYourWatchlistDetails.text = tratamientoDeFechas(mediaItem.fechaEstreno)+ " - " + treatDuration(mediaItem.duracion)
            }
            is SerieMongoItem -> {
                //Ver como hacer que se pueda mandar a otro fragment una lista de elementos data class (temporadas)
                val action = YourWatchlistDetailedFragmentDirections
                    .actionYourWatchlistDetailedFragmentToYourWatchlistEpisodesFragment(mediaItem.id)
                binding.btnMostrarTemporadas.isVisible = true
                binding.tvGeneralInfoYourWatchlistDetails.text = tratamientoDeFechas(mediaItem.fechaEstreno)+ " - " + treatSeasons(mediaItem.numeroTemporadas)
                binding.btnMostrarTemporadas.setOnClickListener {
                    findNavController().navigate(action)
                    Log.i("Ver temporadas", "Click en ver las temporadas de ${mediaItem.nombre}")
                }
            }
        }

        binding.btnDeleteMediaFromYourWatchlist.setOnClickListener {
            showDialogElimination(mediaItem)
        }

        binding.btnChangeStateMediaFromYourWatchlist.setOnClickListener {
            showDialogModification(mediaItem)
        }
    }

    private fun setSpinnerMedia(mediaItem: ItemMediaMongoItem){
        val opciones = listOf(getString(R.string.spinnerStateGeneralInfo), getString(R.string.spinnerStateCast))
        val opcionesAdapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, opciones)
        opcionesAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        binding.spinnerYourWatchlistDetails.adapter = opcionesAdapter
        binding.spinnerYourWatchlistDetails.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener{
                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                    if(position == 0){
                        binding.conLayoutGeneralInfo.isVisible = true
                        binding.conLayoutCastInfo.isVisible = false
                        Log.i("Spinner YourWatchlistDetailed", "Seleccionada la ficha general")
                    } else {
                        binding.conLayoutGeneralInfo.isVisible = false
                        binding.conLayoutCastInfo.isVisible = true
                        Log.i("Spinner YourWatchlistDetailed", "Seleccionado en reparto")
                        viewModel.fetchCast(mediaItem)
                    }
                }

                override fun onNothingSelected(parent: AdapterView<*>?) {
                    //Nada XDXDXDDDDD
                }
            }
    }

    //
    private fun showDialogElimination(mediaItem: ItemMediaMongoItem){
        val dialog = Dialog(requireContext())
        val binding = DialogTaskDeletefromyourwatchlistBinding.inflate(layoutInflater)
        dialog.setContentView(binding.root)

        binding.tvHeaderDeleteFromYourWatchlist.text = getString(R.string.dialogTextHeaderDeletion) + "${mediaItem.nombre}:"

        when(mediaItem){
            is MovieMongoItem -> {
                binding.tvBodyDeleteFromYourWatchlist.text = getString(R.string.dialogTextBodyDeletionPart1Movies) + " ${mediaItem.nombre} " + getString(R.string.dialogTextBodyDeletionPart2Both)
            }
            is SerieMongoItem -> {
                binding.tvBodyDeleteFromYourWatchlist.text =  getString(R.string.dialogTextBodyDeletionPart1Series) + " ${mediaItem.nombre} " + getString(R.string.dialogTextBodyDeletionPart2Both)
            }
        }

        //Hacer la función de eliminación más responsiva
        binding.btnDialogDeleteFromYourWatchlistConfirm.setOnClickListener {
            Log.i("Confirmacion borrado", "El usuario confirma que quiere borrar ${mediaItem.nombre}")
            viewModel.deleteMedia(nombreUsuario, mediaItem)
            dialog.dismiss()
            showDialogDeletionConfirmed(mediaItem.nombre)
        }

        binding.btnDialogDeleteFromYourWatchlistCancel.setOnClickListener {
            Log.i("Cancelación de borrado", "El usuario cancela la eliminación de ${mediaItem.nombre}")
            dialog.dismiss()
        }

        dialog.show()
    }

    private fun showDialogModification(mediaItem: ItemMediaMongoItem){
        val dialog = Dialog(requireContext())
        val binding = DialogTaskChangestatefrommediaBinding.inflate(layoutInflater)

        dialog.setContentView(binding.root)
        when(mediaItem){
            is MovieMongoItem -> {
                binding.rbHalfWatched.isVisible = false;
            }
            is SerieMongoItem -> {
                Log.i("Chando", "LMSOOO")
            }
        }

        binding.btnConfirmChanges.setOnClickListener {
            val selectedID = binding.rgStatesOfMedia.checkedRadioButtonId
            val estado = when(selectedID){
                R.id.rbWatching -> "Viendo"
                R.id.rbWatched -> "Vista"
                R.id.rbHalfWatched -> "A medias"
                R.id.rbWatchlist -> "Por ver"
                else -> null
            }
            Log.i("Confirmación de cambio", "El usuario confirma el cambio de ${mediaItem.nombre} poniendolo en ${estado}")
            dialog.dismiss()
            if(estado != null){
                viewModel.changeStateMedia(nombreUsuario, mediaItem, estado)
            }
        }
        dialog.show()
    }

    //Funcion que se usa para mostrar un icono de eliminacion
    private fun showDialogDeletionConfirmed(nombreMedia: String){
        val dialog = Dialog(requireContext())
        val binding = DialogTaskDeletionconfirmedBinding.inflate(layoutInflater)

        dialog.setContentView(binding.root)
        //Pendiente cambiar el texto de eliminación confirmada
        binding.tvBodyDeletionConfirmed.text = getString(R.string.dialogTextBodyConfirmedDeletionPart1) +
                nombreMedia + getString(R.string.dialogTextBodyConfirmedDeletionPart2)

        binding.btnDialogDeletionConfirmed.setOnClickListener {
            Log.i("Chando", "Cerrar pestaña")
            dialog.dismiss()
            findNavController().popBackStack()
        }

        dialog.show()
    }

    private fun treatSeasons(numeroTemporadas: Int): String{
        if(numeroTemporadas > 1){
            return "${numeroTemporadas}" + getString(R.string.seasonsPlural)
        } else {
            return "${numeroTemporadas}" + getString(R.string.seasonsSingular)
        }
    }

    private fun treatDuration(minutos: Int): String{
        if(minutos < 59){
            return "${minutos} minutos"
        } else {
            val horas = minutos / 60
            val minRes = minutos % 60
            if(horas > 1){
                return horas.toString() + getString(R.string.hoursPlural) +
                        minRes+ getString(R.string.minutesPlural)
            } else {
                return horas.toString() + getString(R.string.hoursSingular) +
                        minRes + getString(R.string.minutesPlural)
            }
        }
    }
    private fun tratamientoDeFechas(fechaString: String?): String{
        val fecha = LocalDate.parse(fechaString, DateTimeFormatter.ISO_LOCAL_DATE)
        return "${fecha.dayOfMonth} ${fecha.month} ${fecha.year}"
    }
}
//Lista de pendientes
//-Hacer pruebas de muestras de episodios con series terminadas(Cowboy bebop) como en curso (Cinderrella Gray)
//Mayormente ya está todo terminado, si acaso se podrian agregar detalles de responsibidad