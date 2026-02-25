package com.example.tfgwatchlist.login.ui.views.RegistrationScreen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.tfgwatchlist.login.domain.LoginAndRegisterRepository
import com.example.tfgwatchlist.login.ui.screens.getLoginAndRegisterRepository
import com.example.tfgwatchlist.login.ui.views.loginScreen.LoginScreenViewModel
import com.example.tfgwatchlist.yourwatchlist.ui.screens.getYourWatchlistRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch

class RegistrationScreenViewModel(private val repository: LoginAndRegisterRepository): ViewModel() {

    private val _uiState = MutableStateFlow<RegistrationScreenUiState>(
        RegistrationScreenUiState.Empty
    )

    val uiState = _uiState.asStateFlow()

    //Función para registrar usuarios en la base de datos de los cojines
    fun registerUser(username: String, password: String){
        viewModelScope.launch(Dispatchers.IO) {
            repository.registerUser(username, password)
                .onStart { _uiState.value = RegistrationScreenUiState.Loading }
                .catch {
                    _uiState.value = RegistrationScreenUiState.Error(it.message.toString())
                }
                .collect {
                    _uiState.value = RegistrationScreenUiState.registerResponse(it)
                }
        }
    }

    companion object{
        val Factory = viewModelFactory {
            initializer {
                RegistrationScreenViewModel(this.getLoginAndRegisterRepository())
            }
        }
    }
}