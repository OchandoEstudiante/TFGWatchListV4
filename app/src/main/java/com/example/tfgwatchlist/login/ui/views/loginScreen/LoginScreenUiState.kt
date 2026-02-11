package com.example.tfgwatchlist.login.ui.views.loginScreen

import com.example.tfgwatchlist.login.data.models.LoginResponse
import com.example.tfgwatchlist.yourwatchlist.data.network.model.CastMongoItem
import com.example.tfgwatchlist.yourwatchlist.ui.views.YourWatchlistDetailed.YourWatchlistDetailedUiState

sealed class LoginScreenUiState {
    data object Loading: LoginScreenUiState()
    data object Empty: LoginScreenUiState()
    data class Error(val message: String): LoginScreenUiState()
    data class loginResponse(val response: LoginResponse): LoginScreenUiState()
    //data class Success(val casts: List<CastMongoItem>): YourWatchlistDetailedUiState()
}