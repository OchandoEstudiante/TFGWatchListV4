package com.example.tfgwatchlist.watchlist.ui.views.watchlistDetailed

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.tfgwatchlist.watchlist.data.network.model.MediaItemDetails
import com.example.tfgwatchlist.watchlist.data.network.model.PeliculaDetailsResponse
import com.example.tfgwatchlist.watchlist.data.network.model.SerieDetailsResponse
import com.example.tfgwatchlist.watchlist.domain.WatchlistRepository
import com.example.tfgwatchlist.watchlist.ui.screens.getWatchlistRepository
import com.example.tfgwatchlist.yourwatchlist.data.network.YourWatchlistRepositoryImpl
import com.example.tfgwatchlist.yourwatchlist.domain.YourWatchlistRepository
import com.example.tfgwatchlist.yourwatchlist.ui.screens.getYourWatchlistRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch

class WatchlistDetailsViewModel(private val repository: WatchlistRepository,
private val mongoRepository: YourWatchlistRepository): ViewModel() {
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
            mongoRepository.checkServer()
        }
    }

    fun addMediaToYourWatchlist(media: MediaItemDetails, userId: String){

        viewModelScope.launch(Dispatchers.IO) {
            when(media){
                is PeliculaDetailsResponse -> {
                    mongoRepository.addMovieToYourWatchlist(media.id.toString(), userId)
                        .onStart { _uiState.value = WatchlistDetailsUiState.Empty }
                        .catch {
                            _uiState.value = WatchlistDetailsUiState.Error("Se ha producido un error con la agregación de la película: ${it.message}")
                            Log.i("Chando", _uiState.value.toString())
                        }
                        .collect {
                            _uiState.value = WatchlistDetailsUiState.SuccessPostPelicula(it)
                        }
                }
                is SerieDetailsResponse -> {
                    mongoRepository.addSerieToYourWatchlist(media.id.toString(), userId)
                        .onStart { _uiState.value = WatchlistDetailsUiState.Empty }
                        .catch {
                            _uiState.value = WatchlistDetailsUiState.Error("Se ha producido un error con la agregación de la serie: ${it.message}")
                            Log.i("Chando", _uiState.value.toString())
                        }
                        .collect {
                            _uiState.value = WatchlistDetailsUiState.SuccessPostSerie(it)
                        }
                }
            }
        }
    }
    companion object{
        val Factory = viewModelFactory {
            initializer {
                WatchlistDetailsViewModel(
                    repository = this.getWatchlistRepository(),
                    mongoRepository = this.getYourWatchlistRepository())
            }
        }
    }
}