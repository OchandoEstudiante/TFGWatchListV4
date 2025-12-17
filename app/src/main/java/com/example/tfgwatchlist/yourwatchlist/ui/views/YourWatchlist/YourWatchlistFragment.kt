package com.example.tfgwatchlist.yourwatchlist.ui.views.YourWatchlist

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.appcompat.widget.SearchView
import androidx.compose.ui.graphics.BlurEffect
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.tfgwatchlist.R
import com.example.tfgwatchlist.databinding.FragmentWatchlistDetailedBinding
import com.example.tfgwatchlist.databinding.FragmentYourWatchlistBinding
import com.example.tfgwatchlist.yourwatchlist.data.network.model.ItemMediaMongoItem
import com.example.tfgwatchlist.yourwatchlist.data.network.model.MovieMongoItem
import com.example.tfgwatchlist.yourwatchlist.data.network.model.SerieMongoItem
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch


class YourWatchlistFragment : Fragment() {

    private lateinit var binding: FragmentYourWatchlistBinding

    private lateinit var modo: String

    private val viewModel by viewModels<YourWatchlistViewModel>{
        YourWatchlistViewModel.Factory
    }

    private val adapter by lazy { YourWatchlistAdapter{onItemClick(it)} }

    private fun onItemClick(item: ItemMediaMongoItem){
        when(item){
            is MovieMongoItem -> {
                Log.i("Chando", "Click en serie de tu watchlist: ${item.nombre}")
                val action = YourWatchlistFragmentDirections
                    .actionYourWatchlistFragmentToYourWatchlistDetailedFragment(item)
                findNavController().navigate(action)
            }
            is SerieMongoItem -> {
                Log.i("Chando", "Click en serie de tu watchlist: ${item.nombre}")
                val action = YourWatchlistFragmentDirections
                    .actionYourWatchlistFragmentToYourWatchlistDetailedFragment(item)
                findNavController().navigate(action)
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentYourWatchlistBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initUI()
        observeUI()

        binding.rvWatchlistSearch.layoutManager = LinearLayoutManager(requireContext())
        binding.rvWatchlistSearch.adapter = adapter
        modo = "Series"
        viewModel.fetchSeries()
    }

    private fun observeUI(){
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED){
                viewModel.uiState.collectLatest { uiState ->
                    binding.pbYourWatchlist.isVisible = false
                    when(uiState){
                        YourWatchlistUiState.Empty -> {
                            Log.i("Chando", "Vacio")
                            binding.tvWatchlistSearchInfo.text = ""
                        }
                        YourWatchlistUiState.Loading -> {
                            binding.rvWatchlistSearch.isVisible = false
                            binding.pbYourWatchlist.isVisible = true
                            binding.tvWatchlistSearchInfo.text = ""
                            Log.i("Chando", "Cargando tu watchlist")
                        }
                        is YourWatchlistUiState.Error -> {
                            //Pendiente agregar que saque por pantalla el fallo o algo no se
                            Log.i("Chando", "Error en tu watchlist ${uiState.message}")
                            binding.tvWatchlistSearchInfo.text = uiState.message
                        }
                        is YourWatchlistUiState.Success -> {
                            if(modo == "Series"){
                                if(uiState.items.size == 0){
                                    binding.rvWatchlistSearch.isVisible = false
                                    binding.tvWatchlistSearchInfo.text = "No se han podido encontrar series"
                                } else {
                                    binding.rvWatchlistSearch.isVisible = true
                                    binding.tvWatchlistSearchInfo.text = ""
                                    adapter.submitList(uiState.items)
                                }
                            } else {
                                if(uiState.items.size == 0){
                                    binding.rvWatchlistSearch.isVisible = false
                                    binding.tvWatchlistSearchInfo.text = "No se han podido encontrar películas"
                                } else {
                                    binding.rvWatchlistSearch.isVisible = true
                                    binding.tvWatchlistSearchInfo.text = ""
                                    adapter.submitList(uiState.items)
                                }
                            }
                            Log.i("Chando", "Exito en tu watchlist")
                        }
                    }
                }
            }
        }
    }

    private fun initUI(){
        //Pendiente programar el cambio de iconos
        binding.btnChangeWatchlistSearch.setOnClickListener {
            Log.i("Chando", "Click en cambio")
            if(modo == "Series"){
                modo = "Peliculas"
                binding.btnChangeWatchlistSearch.setImageResource(R.drawable.ic_movie)
            } else {
                modo = "Series"
                binding.btnChangeWatchlistSearch.setImageResource(R.drawable.ic_television)
            }
        }

        binding.btnReloadWatchlistSearch.setOnClickListener {
            if(modo == "Series"){
                viewModel.fetchSeries()
            } else {
                viewModel.fetchPeliculas()
            }
            Log.i("Chando", "Click en recarga")
            searchLocal()
        }
        setSpinner()
        setSearchBar()
    }

    //Pendiente programar el spinner
    private fun setSpinner(){
        val estados = listOf(getString(R.string.statusMediaTextAll),getString(R.string.statusMediaTextWatching) ,
            getString(R.string.statusMediaTextWatched), getString(R.string.statusMediaTextHalfWatched),
            getString(R.string.statusMediaTextToBeSeen))
        val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, estados)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.spFiltersYourWatchlist.adapter = adapter
        binding.spFiltersYourWatchlist.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener{
                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                    Log.i("Filtro", estados[position])
                }

                override fun onNothingSelected(parent: AdapterView<*>?) {
                    //
                }
            }
    }

    private fun setSearchBar(){
        val spinner = binding.spFiltersYourWatchlist
        binding.svWatchlistSearch.setOnQueryTextListener(object : SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(query: String?): Boolean {
                if(spinner.selectedItem.toString() == "Todas"){
                    Log.i("Chando", "Sin filtros")
                } else {
                    Log.i("Chando", "${spinner.selectedItem}")
                }

                Log.i("Busqueda local", "${query} ${spinner.selectedItem}")
                return false
            }
            override fun onQueryTextChange(newText: String?): Boolean {
                return false
            }
        })
    }

    //Funcion general de busqueda
    private fun searchLocal(){
        val spinner = binding.spFiltersYourWatchlist
        val searchBar = binding.svWatchlistSearch
        if(searchBar.query.toString().length == 0){
            if(spinner.selectedItem.toString() == "Todas"){
                Log.i("searchLocal", "Spinner: ${spinner.selectedItem} Searchbar: ${searchBar.query}")
                Log.i("searchLocal", "Sin Query sin filtro")
                if(modo == "Series"){
                    viewModel.fetchSeries()
                } else {
                    viewModel.fetchPeliculas()
                }
            } else {
                if(modo == "Series"){

                } else {

                }
                Log.i("searchLocal", "Spinner: ${spinner.selectedItem} Searchbar: ${searchBar.query}")
                Log.i("searchLocal", "Sin Query Con filtro")
            }
        } else {
            if(spinner.selectedItem.toString() == "Todas"){
                Log.i("searchLocal", "Spinner: ${spinner.selectedItem} Searchbar: ${searchBar.query}")
                Log.i("searchLocal", "Con Query sin filtro")
            } else {
                Log.i("searchLocal", "Spinner: ${spinner.selectedItem} Searchbar: ${searchBar.query}")
                Log.i("searchLocal", "Con Query con filtro")
            }
        }
    }
}

//Pendiente:
//-Programación de las busquedas de query y filtros
//-Poner un texto informatico en caso de errores, timeouts, falta de respuesta, etc
//-Hacer que el boton cambie de icono al hacerle click
