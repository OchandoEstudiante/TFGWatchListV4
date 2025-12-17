package com.example.tfgwatchlist.yourwatchlist.ui.views.YourWatchlist

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
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

class YourWatchlistViewModel(private val repository: YourWatchlistRepository): ViewModel() {

    private val _uiState = MutableStateFlow<YourWatchlistUiState>(YourWatchlistUiState.Empty)
    val uiState = _uiState.asStateFlow()

    //Devuelve todas las series de tu watchlist
    fun checkServer(){
        viewModelScope.launch(Dispatchers.IO) {
            try{
                val response = repository.checkServer()
                Log.i("Check", "${response}")
            } catch (e: Exception){
                Log.i("Error", "Error al realizar conexión")
            }
        }
    }
    fun fetchSeries(){
        viewModelScope.launch(Dispatchers.IO) {
            repository.fetchSeriesFlow()
                .onStart{_uiState.value = YourWatchlistUiState.Loading }
                .catch{
                    Log.i("Chando", "Error en la linea 27 del view model de tu watchlist")
                    _uiState.value = YourWatchlistUiState.Error("Se ha producido un error ${it.message}")
                }
                .collect{
                    Log.i("Chando", "Obtenidos los datos")
                    _uiState.value = YourWatchlistUiState.Success(it)
                }
        }
    }

    fun fetchPeliculas(){
        viewModelScope.launch(Dispatchers.IO) {
            repository.fetchPeliculasFlow()
                .onStart { _uiState.value = YourWatchlistUiState.Loading }
                .catch {
                    Log.i("Peliculas Mongo error", "Error al obtener las películas")
                    _uiState.value = YourWatchlistUiState.Error("Se ha producido un error ${it.message}")
                }
                .collect {
                    Log.i("Chanod", "")
                    _uiState.value = YourWatchlistUiState.Success(it)
                }
        }
    }

    fun fetchMoviesFiltered(filtro: String, query: String){
        viewModelScope.launch(Dispatchers.IO) {

        }
    }

    fun fetchSeriesFiltered(filtro: String, query: String){
        viewModelScope.launch(Dispatchers.IO) {

        }
    }

    companion object{
        val Factory = viewModelFactory {
            initializer {
                //Crear el extention extras de los cojones en casa para el repository de mongo así como el adapter y el viewHolder y probar a hacer la busqueda
                YourWatchlistViewModel(this.getYourWatchlistRepository())
            }
        }
    }
}