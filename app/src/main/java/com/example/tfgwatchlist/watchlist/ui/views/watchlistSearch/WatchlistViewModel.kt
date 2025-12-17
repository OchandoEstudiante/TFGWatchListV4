package com.example.tfgwatchlist.watchlist.ui.views.watchlistSearch

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.example.tfgwatchlist.databinding.ItemWatchlistsearchBinding
import com.example.tfgwatchlist.watchlist.data.network.model.MediaItem
import com.example.tfgwatchlist.watchlist.domain.WatchlistRepository
import com.example.tfgwatchlist.watchlist.ui.screens.getWatchlistRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch

class WatchlistViewModel(private val repository: WatchlistRepository) : ViewModel(){

    private val _uiState = MutableStateFlow<WatchlistUiState>(WatchlistUiState.Empty)

    val uiState = _uiState.asStateFlow()

    fun fetchItems(query: String){
        viewModelScope.launch (Dispatchers.IO){
            repository.fetchItemsFlow(query)
                .onStart {_uiState.value = WatchlistUiState.Loading }
                .catch{
                    Log.i("Chando", "Error en la linea 23, archivo ViewModel ${it.message}")
                    _uiState.value = WatchlistUiState.Error("Se ha producido un error:${it.message}")
                }
                .collect{
                    _uiState.value = WatchlistUiState.Success(it)
                    Log.i("Chando", _uiState.value.toString())
                }
        }
    }

    fun fetchPeliculas(query: String){
        viewModelScope.launch {
            repository.fetchPeliculasFlow(query)
                .onStart { _uiState.value = WatchlistUiState.Loading }
                .catch {
                    Log.i("Chando", "Error en la linea 46 archivo viewModel ${it.message}")
                    _uiState.value = WatchlistUiState.Error("Se ha producido un error: ${it.message}")
                }
                .collect {
                    _uiState.value = WatchlistUiState.Success(it)
                    Log.i("Chando", _uiState.value.toString())
                }
        }
    }

    fun fetchSeries(query: String){
        viewModelScope.launch {
            repository.fetchSeriesFlow(query)
                .onStart { _uiState.value = WatchlistUiState.Loading }
                .catch {
                    Log.i("Chando", "Error en la linea 63 archivo viewModel ${it.message}")
                    _uiState.value = WatchlistUiState.Error("Se ha producido un error: ${it.message}")
                }
                .collect {
                    _uiState.value = WatchlistUiState.Success(it)
                    Log.i("Chando", _uiState.value.toString())
                }
        }
    }

    companion object{
        val Factory = viewModelFactory {
            initializer {
                WatchlistViewModel(this.getWatchlistRepository())
            }
        }
    }
}