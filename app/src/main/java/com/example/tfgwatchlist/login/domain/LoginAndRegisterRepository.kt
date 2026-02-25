package com.example.tfgwatchlist.login.domain

import android.provider.ContactsContract
import com.example.tfgwatchlist.login.data.models.LoginResponse
import com.example.tfgwatchlist.login.data.models.RegisterResponse
import kotlinx.coroutines.flow.Flow

interface LoginAndRegisterRepository {

    suspend fun loginUser(
        username: String,
        password: String
    ): Flow<LoginResponse>

    suspend fun registerUser(
        username: String,
        password: String
    ): Flow<RegisterResponse>
}