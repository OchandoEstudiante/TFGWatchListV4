package com.example.tfgwatchlist.login.ui.views.loginScreen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.tfgwatchlist.core.datastore.AuthDataStore
import com.example.tfgwatchlist.login.domain.LoginAndRegisterRepository
import com.example.tfgwatchlist.login.ui.screens.getLoginAndRegisterRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch

class LoginScreenViewModel(
    private val repository: LoginAndRegisterRepository,
    private val authDataStore: AuthDataStore
): ViewModel() {

    private val _uiState = MutableStateFlow<LoginScreenUiState>(
        LoginScreenUiState.Empty
    )

    val uiState = _uiState.asStateFlow()

    fun loginUser(username: String, password: String){
        viewModelScope.launch(Dispatchers.IO) {
            repository.loginUser(username, password)
                .onStart { _uiState.value = LoginScreenUiState.Loading }
                .catch {
                    _uiState.value = LoginScreenUiState.Error(it.message.toString())
                }
                .collect {
                    authDataStore.saveLoginState(true)
                    authDataStore.saveToken(username)

                    _uiState.value = LoginScreenUiState.loginResponse(it)
                }
        }
    }

    companion object{
        val Factory = viewModelFactory {
            initializer {
                val application = this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY]!!
                LoginScreenViewModel(
                    this.getLoginAndRegisterRepository(),
                    AuthDataStore(application.applicationContext))
            }
        }
    }
}