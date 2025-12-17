package com.example.tfgwatchlist.watchlist.ui.views.watchlistDetailed

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.tfgwatchlist.watchlist.data.network.model.MediaItemDetails
import com.example.tfgwatchlist.watchlist.data.network.model.SerieDetailsResponse
import com.example.tfgwatchlist.watchlist.domain.WatchlistRepository
import com.example.tfgwatchlist.watchlist.ui.screens.getWatchlistRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch

class WatchlistDetailsViewModel(private val repository: WatchlistRepository): ViewModel() {

    private val _uiState = MutableStateFlow<WatchlistDetailsUiState>(WatchlistDetailsUiState.Empty)

    val uiState = _uiState.asStateFlow()

    fun fetchSeriesDetails(id: Int){
        viewModelScope.launch(Dispatchers.IO) {
            repository.fetchSerieDetails(id.toString())
                .onStart { _uiState.value = WatchlistDetailsUiState.Loading }
                .catch {
                    _uiState.value = WatchlistDetailsUiState.Error("Se ha producido un error con la serie: ${it.message}")
                    Log.i("Chando", _uiState.value.toString())
                }
                .collect {
                    _uiState.value = WatchlistDetailsUiState.SuccessMedia(it)
                    Log.i("Chando", _uiState.value.toString())
                }
        }
    }

    fun fetchMovieDetails(id: Int){
        viewModelScope.launch(Dispatchers.IO) {
            repository.fetchPeliculaDetails(id.toString())
                .onStart { _uiState.value = WatchlistDetailsUiState.Loading }
                .catch {
                    _uiState.value = WatchlistDetailsUiState.Error("Se ha producido un error con la pelicula: ${it.message}")
                    Log.i("Chando", _uiState.value.toString())
                }
                .collect {
                    _uiState.value = WatchlistDetailsUiState.SuccessMedia(it)
                    Log.i("Chando", _uiState.value.toString())
                }
        }
    }

    fun fetchActorsDetails(){
        viewModelScope.launch(Dispatchers.IO) {

        }
    }

    fun addMediaToYourWatchlist(media: MediaItemDetails){
        viewModelScope.launch(Dispatchers.IO) {

        }
    }
    companion object{
        val Factory = viewModelFactory {
            initializer {
                WatchlistDetailsViewModel(this.getWatchlistRepository())
            }
        }
    }
}