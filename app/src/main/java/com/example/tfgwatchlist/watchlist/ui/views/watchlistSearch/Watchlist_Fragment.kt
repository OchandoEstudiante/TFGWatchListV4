package com.example.tfgwatchlist.watchlist.ui.views.watchlistSearch

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.SearchView
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.tfgwatchlist.R
import com.example.tfgwatchlist.databinding.FragmentWatchlistBinding
import com.example.tfgwatchlist.watchlist.data.network.model.MediaItem
import com.example.tfgwatchlist.watchlist.data.network.model.PeliculaResponse
import com.example.tfgwatchlist.watchlist.data.network.model.SerieResponse
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER

class Watchlist_Fragment : Fragment() {

    //
    private lateinit var binding: FragmentWatchlistBinding
    private lateinit var modo: String

    private val viewModel by viewModels<WatchlistViewModel>{
        WatchlistViewModel.Factory
    }

    //private lateinit var adapter: WatchlistAdapter
    private val adapter by lazy { WatchlistAdapter {onItemClick(it)} }

    private fun onItemClick(item: MediaItem){
        when(item){
            is PeliculaResponse -> {
                Log.i("Chando", "Click en serie:${item.titulo}")
                val action = Watchlist_FragmentDirections
                    .actionWatchlistFragmentToWatchlistDetailedFragment(mediaId = item.id, typeMedia = "Peliculas")
                findNavController().navigate(action)
            }
            is SerieResponse -> {
                Log.i("Chando", "Click en serie:${item.titulo}")
                val action = Watchlist_FragmentDirections
                    .actionWatchlistFragmentToWatchlistDetailedFragment(mediaId = item.id, typeMedia = "Series")
                findNavController().navigate(action)
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentWatchlistBinding.inflate(inflater, container, false)
        return binding.root
        // Inflate the layout for this fragment
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initUI()
        observeUI()

        binding.rvWatchlistSearch.layoutManager = LinearLayoutManager(requireContext())
        binding.rvWatchlistSearch.adapter = adapter
    }

    private fun observeUI(){
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED){
                viewModel.uiState.collectLatest { uiState ->
                    binding.pbWatchlistSearch.isVisible = false
                    when(uiState){
                        WatchlistUiState.Empty -> {
                            Log.i("Chando", "Estado vacio")
                            binding.tvWatchlistSearchInfo.text = "Busca en la barra"
                        }
                        is WatchlistUiState.Error -> {
                            Log.i("Chando", "Estado de error ${uiState.message}")
                            Snackbar.make(binding.root, uiState.message, Snackbar.LENGTH_SHORT).show()
                            binding.rvWatchlistSearch.isVisible = false
                            binding.tvWatchlistSearchInfo.text = "Sample error"
                        }
                        WatchlistUiState.Loading -> {
                            Log.i("Chando", "Cargando")
                            binding.rvWatchlistSearch.isVisible = false
                            binding.pbWatchlistSearch.isVisible = true
                            binding.tvWatchlistSearchInfo.text = ""
                        }
                        is WatchlistUiState.Success -> {
                            if(modo == "Series"){
                                if(uiState.items.size == 0){
                                    binding.rvWatchlistSearch.isVisible = false
                                    binding.tvWatchlistSearchInfo.text = "No se pudieron encontrar series"
                                } else {
                                    Log.i("Series", "Funciona: ${uiState.items}")
                                    binding.rvWatchlistSearch.isVisible = true
                                    binding.tvWatchlistSearchInfo.text = ""
                                    adapter.submitList(uiState.items)
                                }
                            } else {
                                if(uiState.items.size == 0){
                                    binding.rvWatchlistSearch.isVisible = false
                                    binding.tvWatchlistSearchInfo.text = "No se pudieron encontrar peliculas"
                                } else {
                                    Log.i("Peliculas", "Funciona: ${uiState.items}")
                                    binding.rvWatchlistSearch.isVisible = true
                                    binding.tvWatchlistSearchInfo.text = ""
                                    adapter.submitList(uiState.items)
                                }
                            }
                            /*
                            if(uiState.items.size == 0){
                                binding.rvWatchlistSearch.isVisible = false
                                binding.tvWatchlistSearchInfo.text = "Sample"
                            } else {
                                Log.i("Chando", "Funciona: ${uiState.items.toString()}")
                                binding.rvWatchlistSearch.isVisible = true
                                binding.tvWatchlistSearchInfo.text = ""
                                adapter.submitList(uiState.items)
                            }*/
                        }
                    }
                }
            }
        }
    }

    private fun initUI(){
        modo = "Series"
        binding.svWatchlistSearch.setOnQueryTextListener(object: SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(query: String): Boolean {
                if(modo == "Series"){
                    Log.i("Series", "Busqueda de series")
                    viewModel.fetchSeries(query)
                } else {
                    Log.i("Peliculas", "Busqueda de peliculas")
                    viewModel.fetchPeliculas(query)
                }
                //viewModel.fetchItems(query);
                //Log.i("Chando", "Busqueda ${query}")
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                return false
            }
        })
        Log.i("Chando", "Inicios UI")
        binding.btnChangeWatchlistSearch.setOnClickListener {
            if(modo == "Series"){
                modo = "Peliculas"
                binding.btnChangeWatchlistSearch.setImageResource(R.drawable.ic_movie)
            } else {
                modo = "Series"
                binding.btnChangeWatchlistSearch.setImageResource(R.drawable.ic_television)
            }
            Log.i("Chando", "Click en cambiar modo")
        }
    }
}