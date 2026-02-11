package com.example.tfgwatchlist.login.ui.views.RegistrationScreen

import android.os.Message
import com.example.tfgwatchlist.login.data.models.RegisterResponse

sealed class RegistrationScreenUiState {
    data object Loading: RegistrationScreenUiState()
    data object Empty: RegistrationScreenUiState()
    data class Error(val message: String): RegistrationScreenUiState()
    data class registerResponse(val response: RegisterResponse): RegistrationScreenUiState()
}