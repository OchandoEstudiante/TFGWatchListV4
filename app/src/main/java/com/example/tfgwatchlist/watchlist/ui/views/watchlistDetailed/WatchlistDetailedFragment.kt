package com.example.tfgwatchlist.watchlist.ui.views.watchlistDetailed

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
import androidx.fragment.app.activityViewModels
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
import com.example.tfgwatchlist.databinding.DialogTaskAddtoyourwatchlistBinding
import com.example.tfgwatchlist.databinding.FragmentWatchlistDetailedBinding
import com.example.tfgwatchlist.shared.UserViewModel
import com.example.tfgwatchlist.watchlist.data.network.model.MediaItemDetails
import com.example.tfgwatchlist.watchlist.data.network.model.PeliculaDetailsResponse
import com.example.tfgwatchlist.watchlist.data.network.model.SerieDetailsResponse
import com.example.tfgwatchlist.yourwatchlist.domain.YourWatchlistRepository
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import kotlin.getValue

class WatchlistDetailedFragment : Fragment() {

    /*
    -Tratar las fechas aquí también()
    -Hacer el adapter de los actores y que funcione(Solucionado(Estaría bien mejorar el formato))
    -Mejorar el formato visual de los episodios(WIP)
     */
    private lateinit var binding: FragmentWatchlistDetailedBinding

    private lateinit var adapterReparto: WatchlistDetailsAdapter
    private lateinit var nombreUsuario: String
    private val userViewModel: UserViewModel by activityViewModels()
    private val viewModel by viewModels<WatchlistDetailsViewModel>{
        WatchlistDetailsViewModel.Factory
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentWatchlistDetailedBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val authDataStore = AuthDataStore(requireContext())
        lifecycleScope.launch {
            authDataStore.userToken.collect { storedUserName ->
                storedUserName?.let {
                    nombreUsuario = storedUserName
                    Log.i("ChandoNameUserDetailedWathclist", nombreUsuario)
                    val idMedia = arguments?.getInt("mediaId")?: -1
                    val typeMedia = arguments?.getString("typeMedia")
                    adapterReparto = WatchlistDetailsAdapter()
                    binding.rvWatchlistDetailsCast.layoutManager = LinearLayoutManager(requireContext())
                    binding.rvWatchlistDetailsCast.adapter = adapterReparto
                    initUI(idMedia, typeMedia)
                    observeUI()
                }
            }
        }
        /*
        nombreUsuario = userViewModel.returnUserName().toString()
        Log.i("ChandoNameUser", nombreUsuario)
        val idMedia = arguments?.getInt("mediaId")?: -1
        val typeMedia = arguments?.getString("typeMedia")
        adapterReparto = WatchlistDetailsAdapter()
        binding.rvWatchlistDetailsCast.layoutManager = LinearLayoutManager(requireContext())
        binding.rvWatchlistDetailsCast.adapter = adapterReparto
        initUI(idMedia, typeMedia)
        observeUI() */
    }

    private fun observeUI(){
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED){
                viewModel.uiState.collectLatest { uiState ->
                    when(uiState){
                        WatchlistDetailsUiState.Empty -> {
                            Log.i("Empty", "XD")
                        }
                        WatchlistDetailsUiState.Loading -> {
                            Log.i("Cargando details", "Cargando...")
                        }

                        is WatchlistDetailsUiState.Error -> {
                            Snackbar.make(binding.root, uiState.message, Snackbar.LENGTH_SHORT).show()
                        }

                        is WatchlistDetailsUiState.SuccessMedia -> {
                            Log.i("Serie exito", "Sacando por pantalla la serie ${uiState.mediaDetails.titulo}")
                            rellenarData(uiState.mediaDetails)
                        }

                        is WatchlistDetailsUiState.SuccessPostPelicula -> {
                            if(uiState.postResponse.ok){
                                Log.i("Película exito", "Agregada con exito la película a la lista de ${nombreUsuario}")
                                Snackbar.make(binding.root, getString(R.string.movieAddedTextPart1)+ " ${uiState.postResponse.movieName}" +getString(R.string.movieAddedTextPart2)+ "${nombreUsuario}", Snackbar.LENGTH_SHORT).show()
                            } else {
                                Snackbar.make(binding.root, getString(R.string.movieAlreadyInWatchlist), Snackbar.LENGTH_SHORT).show()
                            }
                        }
                        is WatchlistDetailsUiState.SuccessPostSerie -> {
                            if(uiState.postResponse.ok){
                                Log.i("Serie exito", "Agregada con exito la serie a la lista de ${nombreUsuario}")
                                Snackbar.make(binding.root, getString(R.string.serieAddedTextPart1)+ " ${uiState.postResponse.serieName}" +getString(R.string.serieAddedTextPart2)+ "${nombreUsuario}", Snackbar.LENGTH_SHORT).show()
                            } else {
                                Snackbar.make(binding.root, getString(R.string.serieAlreadyInWatchlist), Snackbar.LENGTH_SHORT).show()
                            }
                        }
                    }
                }
            }
        }
    }

    private fun initUI(id: Int, tipoMedia: String?){
        if(tipoMedia == "Series"){
            Log.i("Series", "Es una serie")
            viewModel.fetchSeriesDetails(id)
        } else {
            Log.i("Peliculas", "Es una pelicula")
            viewModel.fetchMovieDetails(id)
        }
    }

    private fun rellenarData(media: MediaItemDetails){
        binding.tvWatchlistDetailsTituloMedia.text = media.titulo
        binding.tvWatchlistDetailsPremisaMedia.text = media.premisa


        Glide.with(binding.root.context)
            .load(generarURL("w780${media.poster}"))
            .into(binding.ivWatchlistDetailsImagenPoster)

        Glide.with(binding.root.context)
            .load(generarURL("original${media.banner}"))
            .into(binding.ivWatchlistDetailsImagenBanner)

        if(media.videos?.results?.size == 0){
            Log.i("Chando", "No tiene videos")
            binding.imgBtnVerTrailer.isVisible = false
        } else {
            Log.i("Chando", "Tiene videos")
            val trailer = media.videos?.results[0]?.key
            Glide.with(binding.root.context)
                .load(generarURLYoutube("${trailer}"))
                .into(binding.imgBtnVerTrailer)

            //Al menos la funcionalidad del boton funciona, es algo
            binding.imgBtnVerTrailer.setOnClickListener {
                Log.i("Chando", "Click en ver trailer")
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://www.youtube.com/watch?v=${trailer}"))
                context?.startActivity(intent)
            }
        }

        val generos = media.generos.map { it.name }

        binding.tvWatchlistDetailsGenres.text = generos.toString()

        when(media){
            is PeliculaDetailsResponse -> {
                Log.i("Pelicula", "Es pelicula details")
                rellenarPelicula(media)
            }
            is SerieDetailsResponse -> {
                Log.i("Serie", "Es serie details")
                rellenarSerie(media)
            }
        }

        binding.btnAgregarTuWatchlist.setOnClickListener {
            showDialogue(media)
        }
    }

    private fun rellenarPelicula(pelicula: PeliculaDetailsResponse){
        setSpinnerMedia(pelicula)
        binding.btnMostrarTemporadas.isVisible = false
        binding.tvGeneralInfoWatchlistDetails.text = tratamientoDeFechas(pelicula.fechaEstreno) + " - " + treatDuration(pelicula.duracion)
    }

    private fun rellenarSerie(serie: SerieDetailsResponse){
        setSpinnerMedia(serie)
        binding.btnMostrarTemporadas.isVisible = true
        val action = WatchlistDetailedFragmentDirections
            .actionWatchlistFragmentDetailedToWatchlistEpisodesFragment(serie.id)
        binding.btnMostrarTemporadas.setOnClickListener {
            findNavController().navigate(action)
        }
        binding.tvGeneralInfoWatchlistDetails.text = tratamientoDeFechas(serie.fechaEstreno) + " - " + treatSeasons(serie.cantidadTemporadas)
    }


    private fun setSpinnerMedia(media: MediaItemDetails){
        val opciones = listOf(getString(R.string.spinnerStateGeneralInfo), getString(R.string.spinnerStateCast))
        val opcionesAdapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, opciones)
        opcionesAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        binding.spinnerWatchlistDetails.adapter = opcionesAdapter
        binding.spinnerWatchlistDetails.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener{
                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                    if(position == 0){
                        binding.ConLayoutGeneralInfo.isVisible = true
                        binding.conLayoutCastInfo.isVisible = false
                        Log.i("Chando", "Ficha general")
                    } else {
                        binding.ConLayoutGeneralInfo.isVisible = false
                        binding.conLayoutCastInfo.isVisible = true
                        Log.i("Chando", "Reparto")
                        adapterReparto.submitList(media.credits?.cast)
                    }
                }

                override fun onNothingSelected(parent: AdapterView<*>?) {
                    //
                }

            }
    }

    private fun showDialogue(media: MediaItemDetails){
        val dialog = Dialog(requireContext())
        val binding = DialogTaskAddtoyourwatchlistBinding.inflate(layoutInflater)
        dialog.setContentView(binding.root)

        val btnConfirmation = binding.btnDialogAddToYourWatchlistYes
        val btnNegation = binding.btnDialogAddToYourWatchlistNo

        when(media){
            is PeliculaDetailsResponse -> {
                binding.tvBodyAddToYourWatchlist.text = getString(R.string.dialogConfirmationAddBodyPart1Movies) + media.titulo + getString(R.string.dialogConfirmationAddBodyPart2Both)
            }
            is SerieDetailsResponse -> {
                binding.tvBodyAddToYourWatchlist.text = getString(R.string.dialogConfirmationAddBodyPart1Series) + media.titulo + getString(R.string.dialogConfirmationAddBodyPart2Both)
            }
        }
        btnConfirmation.setOnClickListener {
            Log.i("AddYourWatchlist", "El usuario quiere añadir a watchlist")
            viewModel.addMediaToYourWatchlist(media, nombreUsuario)
            dialog.dismiss()
        }

        btnNegation.setOnClickListener {
            Log.i("DontAddWatchlist", "El usuario no quiere añadir a watchlist")
            dialog.dismiss()
        }

        dialog.show()
    }

    private fun tratamientoDeFechas(fechaString: String?): String{
        val fecha = LocalDate.parse(fechaString, DateTimeFormatter.ISO_LOCAL_DATE)
        return "${fecha.dayOfMonth} ${fecha.month} ${fecha.year}"
    }

    //Funcion para devolver texto según cantidad de temporadas
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
}