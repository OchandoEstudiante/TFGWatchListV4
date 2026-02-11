package com.example.tfgwatchlist.yourwatchlist.ui.views.YourWatchlistDetailed

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.tfgwatchlist.watchlist.data.network.model.MediaItem
import com.example.tfgwatchlist.yourwatchlist.data.network.model.ItemMediaMongoItem
import com.example.tfgwatchlist.yourwatchlist.data.network.model.MovieMongoItem
import com.example.tfgwatchlist.yourwatchlist.data.network.model.SerieMongoItem
import com.example.tfgwatchlist.yourwatchlist.domain.YourWatchlistRepository
import com.example.tfgwatchlist.yourwatchlist.ui.screens.getYourWatchlistRepository
import com.example.tfgwatchlist.yourwatchlist.ui.views.YourWatchlist.YourWatchlistViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch

class YourWatchlistDetailedViewModel(private val repository: YourWatchlistRepository): ViewModel() {

    private val _uiState = MutableStateFlow<YourWatchlistDetailedUiState>(
        YourWatchlistDetailedUiState.Empty)

    val uiState = _uiState.asStateFlow()

    fun fetchCast(mediaItem: ItemMediaMongoItem){
        viewModelScope.launch(Dispatchers.IO) {
            when(mediaItem){
                is MovieMongoItem -> {
                    repository.fetchCastMoviesFlow(mediaItem.id)
                        .onStart { _uiState.value = YourWatchlistDetailedUiState.Loading }
                        .catch {
                            _uiState.value = YourWatchlistDetailedUiState.Error(it.message.toString())
                        }
                        .collect{ _uiState.value = YourWatchlistDetailedUiState.Success(it) }
                }
                is SerieMongoItem -> {
                    repository.fetchCastSeriesFlow(mediaItem.id)
                        .onStart { _uiState.value = YourWatchlistDetailedUiState.Loading }
                        .catch {
                            _uiState.value = YourWatchlistDetailedUiState.Error(it.message.toString())
                        }
                        .collect { _uiState.value = YourWatchlistDetailedUiState.Success(it) }
                }
            }
        }
    }

    fun changeStateMedia(userName: String, mediaItem: ItemMediaMongoItem, estado: String){
        viewModelScope.launch(Dispatchers.IO) {
            when(mediaItem){
                is MovieMongoItem -> {
                    repository.changeStatePelicula(userName, mediaItem.id, estado)
                }
                is SerieMongoItem -> {
                    repository.changeStateSerie(userName, mediaItem.id, estado)
                }
            }
        }
    }

    fun deleteMedia(userName: String, mediaItem: ItemMediaMongoItem){
        viewModelScope.launch(Dispatchers.IO) {
            when(mediaItem){
                is MovieMongoItem -> {
                    repository.deletePelicula(userName, mediaItem.id)
                }
                is SerieMongoItem -> {
                    repository.deleteSerie(userName, mediaItem.id)
                }
            }
        }
    }

    companion object{
        val Factory = viewModelFactory {
            initializer {
                YourWatchlistDetailedViewModel(this.getYourWatchlistRepository())
            }
        }
    }


}