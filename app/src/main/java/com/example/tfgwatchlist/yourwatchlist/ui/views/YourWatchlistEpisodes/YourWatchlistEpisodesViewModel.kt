package com.example.tfgwatchlist.yourwatchlist.ui.views.YourWatchlistEpisodes

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.tfgwatchlist.yourwatchlist.domain.YourWatchlistRepository
import com.example.tfgwatchlist.yourwatchlist.ui.screens.getYourWatchlistRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch

class YourWatchlistEpisodesViewModel(private val repository: YourWatchlistRepository): ViewModel() {

    private val _uiState = MutableStateFlow<YourWatchlistEpisodesUiState>(YourWatchlistEpisodesUiState.Empty)

    val uiState = _uiState.asStateFlow()

    fun fetchSeasonsNames(id: Int){
        viewModelScope.launch(Dispatchers.IO) {
            repository.fetchSeasonsNamesFlow(id)
                .onStart { _uiState.value = YourWatchlistEpisodesUiState.Loading }
                .catch {
                    _uiState.value = YourWatchlistEpisodesUiState.Error("Error ${it.message}")
                }
                .collect {
                    _uiState.value = YourWatchlistEpisodesUiState.SuccessNames(it)
                }
        }
    }
    fun fetchTemporada(id: Int, numeroTemporada: Int){
        viewModelScope.launch(Dispatchers.IO) {
            repository.fetchSeasonSeriesFlow(id, numeroTemporada)
                .onStart { _uiState.value = YourWatchlistEpisodesUiState.Loading }
                .catch {
                    _uiState.value = YourWatchlistEpisodesUiState.Error("Se ha producido un error al sacar la temporada por pantalla ${it.message}")
                }
                .collect {
                    _uiState.value = YourWatchlistEpisodesUiState.Success(it)
                }
        }
    }

    companion object{
        val Factory = viewModelFactory {
            initializer {
                YourWatchlistEpisodesViewModel(this.getYourWatchlistRepository())
            }
        }
    }
}