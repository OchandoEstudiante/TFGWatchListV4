package com.example.tfgwatchlist.watchlist.ui.views.watchlistEpisodes

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.tfgwatchlist.watchlist.data.network.model.SerieDetailsResponse
import com.example.tfgwatchlist.watchlist.domain.WatchlistRepository
import com.example.tfgwatchlist.watchlist.ui.screens.getWatchlistRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch

class WatchlistEpisodesViewModel(private val repository: WatchlistRepository): ViewModel() {

    private val _uiState = MutableStateFlow<WatchlistEpisodesUiState>(WatchlistEpisodesUiState.Empty)

    val uiState = _uiState.asStateFlow()

    fun fetchSerieDetails(id: Int?){
        viewModelScope.launch(Dispatchers.IO) {
            repository.fetchSerieDetails(id.toString())
                .onStart { _uiState.value = WatchlistEpisodesUiState.Loading }
                .catch {
                    _uiState.value = WatchlistEpisodesUiState.Error("Se ha producido un error con la serie: ${it.message}")
                    Log.i("Chando", _uiState.value.toString())
                }
                .collect {
                    _uiState.value = WatchlistEpisodesUiState.Success(it as SerieDetailsResponse)
                    Log.i("Chando", _uiState.value.toString())
                }
        }
    }

    fun fetchEpisodes(id: Int, season: Int){
        viewModelScope.launch(Dispatchers.IO) {
            repository.fetchSerieEpisodes(id.toString(), season)
                .onStart { _uiState.value = WatchlistEpisodesUiState.Empty }
                .catch {
                    _uiState.value = WatchlistEpisodesUiState.ErrorEpisodes("Se ha producido un error al sacar los episodios de la serie: ${it.message}")
                }
                .collect{
                    _uiState.value = WatchlistEpisodesUiState.SuccessEpisodes(it)
                    Log.i("Chando", _uiState.value.toString())
                }
        }
    }

    companion object{
        val Factory = viewModelFactory {
            initializer {
                WatchlistEpisodesViewModel(this.getWatchlistRepository())
            }
        }
    }
}