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
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.tfgwatchlist.R
import com.example.tfgwatchlist.core.utils.generarURL
import com.example.tfgwatchlist.core.utils.generarURLYoutube
import com.example.tfgwatchlist.databinding.DialogTaskAddtoyourwatchlistBinding
import com.example.tfgwatchlist.databinding.FragmentWatchlistDetailedBinding
import com.example.tfgwatchlist.watchlist.data.network.model.MediaItemDetails
import com.example.tfgwatchlist.watchlist.data.network.model.PeliculaDetailsResponse
import com.example.tfgwatchlist.watchlist.data.network.model.SerieDetailsResponse
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class WatchlistDetailedFragment : Fragment() {

    /*
    -Sacar generos por pantalla(Parcialmente solucionado)
    -Tratar las fechas aquí también()
    -Hacer el adapter de los actores y que funcione(Solucionado(Estaría bien mejorar el formato))
    -Hacer que funcione lo de buscar trailers(WIP)
    -Mejorar el formato visual de los episodios(WIP)
    -Hacer que varias de las lineas sean Strings para poder darles traduccion(Hoy mismo)
     */
    private lateinit var binding: FragmentWatchlistDetailedBinding

    private lateinit var adapterReparto: WatchlistDetailsAdapter

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

        val idMedia = arguments?.getInt("mediaId")?: -1
        val typeMedia = arguments?.getString("typeMedia")
        adapterReparto = WatchlistDetailsAdapter()
        binding.rvWatchlistDetailsCast.layoutManager = LinearLayoutManager(requireContext())
        binding.rvWatchlistDetailsCast.adapter = adapterReparto
        initUI(idMedia, typeMedia)
        observeUI()
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
        binding.tvGeneralInfoWatchlistDetails.text = pelicula.fechaEstreno + " - " + pelicula.duracion + "Minutos"
    }

    private fun rellenarSerie(serie: SerieDetailsResponse){
        setSpinnerMedia(serie)
        binding.btnMostrarTemporadas.isVisible = true
        val action = WatchlistDetailedFragmentDirections
            .actionWatchlistFragmentDetailedToWatchlistEpisodesFragment(serie.id)
        binding.btnMostrarTemporadas.setOnClickListener {
            findNavController().navigate(action)
        }
        binding.tvGeneralInfoWatchlistDetails.text = serie.fechaEstreno + " - " + serie.cantidadTemporadas + " temporadas"
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
                binding.tvBodyAddToYourWatchlist.text = "Desea añadir la película '${media.titulo}' a su watchlist?"
            }
            is SerieDetailsResponse -> {
                binding.tvBodyAddToYourWatchlist.text = "Desea añadir la serie '${media.titulo}' a su watchlist?"
            }
        }
        btnConfirmation.setOnClickListener {
            Log.i("AddYourWatchlist", "El usuario quiere añadir a watchlist")
            dialog.dismiss()
        }

        btnNegation.setOnClickListener {
            Log.i("DontAddWatchlist", "El usuario no quiere añadir a watchlist")
            dialog.dismiss()
        }

        dialog.show()
    }

    private fun treatDates(fechaString: String): LocalDate{
        return LocalDate.parse(fechaString, DateTimeFormatter.ISO_LOCAL_DATE)
    }
}