package com.example.tfgwatchlist.watchlist.ui.views.watchlistEpisodes

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
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.tfgwatchlist.core.utils.generarURL
import com.example.tfgwatchlist.databinding.FragmentWatchlistEpisodesBinding
import com.example.tfgwatchlist.watchlist.data.network.model.SerieDetailsResponse
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class WatchlistEpisodesFragment : Fragment() {

    private lateinit var binding: FragmentWatchlistEpisodesBinding

    private lateinit var adapterEpisodios: WatchlistEpisodesAdapter

    private val viewModel by viewModels<WatchlistEpisodesViewModel> {
        WatchlistEpisodesViewModel.Factory
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentWatchlistEpisodesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val idSerie = arguments?.getInt("serieId")?: -1
        adapterEpisodios = WatchlistEpisodesAdapter()
        binding.rvWatchlistEpisodes.layoutManager = LinearLayoutManager(requireContext())
        binding.rvWatchlistEpisodes.adapter = adapterEpisodios
        initUI(idSerie)
    }

    fun observeUI(){
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED){
                viewModel.uiState.collectLatest { uiState ->
                    when(uiState){
                        WatchlistEpisodesUiState.Empty -> {
                            Log.i("Cargando details", "Cargando...")
                        }
                        WatchlistEpisodesUiState.Loading -> {
                            Log.i("Cargando details", "Cargando...")
                            binding.pbEpisodesWatchlist.isVisible = true
                        }
                        is WatchlistEpisodesUiState.Error -> {
                            binding.pbEpisodesWatchlist.isVisible = false
                            Snackbar.make(binding.root, uiState.message, Snackbar.LENGTH_SHORT).show()
                        }
                        is WatchlistEpisodesUiState.ErrorEpisodes -> {
                            binding.pbEpisodesWatchlist.isVisible = false
                            Snackbar.make(binding.root, uiState.message, Snackbar.LENGTH_SHORT).show()
                        }
                        is WatchlistEpisodesUiState.Success -> {
                            binding.pbEpisodesWatchlist.isVisible = false
                            Log.i("Chando", "Saca serie")
                            setSpinner(uiState.serieDetailsResponse)
                        }
                        is WatchlistEpisodesUiState.SuccessEpisodes -> {
                            binding.pbEpisodesWatchlist.isVisible = false
                            adapterEpisodios.submitList(uiState.listaEpisodios)
                        }
                    }
                }
            }
        }
    }

    fun initUI(id: Int){
        viewModel.fetchSerieDetails(id)
        observeUI()
    }

    fun setSpinner(serie: SerieDetailsResponse){
        val temporadas = serie.temporadas
        val nombresTemporadas = serie.temporadas.map { it.tituloTemporada }
        val seasonsAdapter =
            ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, nombresTemporadas)
        seasonsAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.spSeasonsEpisodes.adapter = seasonsAdapter
        if(temporadas.size > 1){
            binding.spSeasonsEpisodes.setSelection(1)
        }
        binding.spSeasonsEpisodes.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener{
                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                    binding.tvTituloTemporadaWatchlistTemporada.text = temporadas[position].tituloTemporada
                    binding.tvPremisaTemporadaWatchlistTemporada.text = temporadas[position].premisaTemporada
                    if(temporadas[0].numeroTemporada == 0){
                        viewModel.fetchEpisodes(serie.id, position)
                    } else {
                        viewModel.fetchEpisodes(serie.id, position + 1)
                    }
                    if(temporadas[position].imagenTemporada != null){
                        Glide.with(binding.root.context)
                            .load(generarURL("w780${temporadas[position].imagenTemporada}"))
                            .into(binding.ivSerieImageWatchlistTemporada)
                    } else {
                        Glide.with(binding.root.context)
                            .load(generarURL("w780${serie.poster}"))
                            .into(binding.ivSerieImageWatchlistTemporada)
                    }
                }

                override fun onNothingSelected(parent: AdapterView<*>?) {
                    //Ola mina XD
                }

            }
    }
}