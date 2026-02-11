package com.example.tfgwatchlist.login.data.network.api

import com.example.tfgwatchlist.login.data.models.LoginRequest
import com.example.tfgwatchlist.login.data.models.LoginResponse
import com.example.tfgwatchlist.login.data.models.RegisterBody
import com.example.tfgwatchlist.login.data.models.RegisterRequest
import com.example.tfgwatchlist.login.data.models.RegisterResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface LoginAndRegisterApiService {
    @POST("usersApp/login")
    suspend fun login(
        @Body request: LoginRequest
    ): Response<LoginResponse>

    @POST("usersApp/AddUser")
    suspend fun register(
        @Body request: RegisterBody
    ): Response<RegisterResponse>
}