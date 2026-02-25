package com.example.tfgwatchlist.login.data.network

import com.example.tfgwatchlist.login.data.models.LoginRequest
import com.example.tfgwatchlist.login.data.models.LoginResponse
import com.example.tfgwatchlist.login.data.models.RegisterBody
import com.example.tfgwatchlist.login.data.models.RegisterRequest
import com.example.tfgwatchlist.login.data.models.RegisterResponse
import com.example.tfgwatchlist.login.data.network.api.LoginAndRegisterApiService
import com.example.tfgwatchlist.login.domain.LoginAndRegisterRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class LoginAndRegisterRepositoryImpl(
    private val apiService: LoginAndRegisterApiService
): LoginAndRegisterRepository {
    override suspend fun loginUser(
        username: String,
        password: String
    ): Flow<LoginResponse> = flow {
        val response = apiService.login(
            LoginRequest(
                nameUser = username,
                passwordUser = password
            )
        )
        if(response.isSuccessful){
            response.body()?.let {loginResponse ->
                emit(loginResponse)
            } ?: Exception("Login response vacío")
        } else {
            throw Exception("Error Login: ${response.code()}")
        }
    }

    override suspend fun registerUser(
        username: String,
        password: String,
    ): Flow<RegisterResponse> = flow{
        val response = apiService.register(
            RegisterBody(
                user = RegisterRequest(
                    name = username,
                    pass = password
                )
            )
        )

        if(response.isSuccessful){
            response.body()?.let{ RegisterResponse ->
                emit(RegisterResponse)
            } ?: Exception("Register response empty")
        } else {
            throw Exception("Error register ${response.code()}")
        }
    }

    /*override suspend fun registerUser(
        id: Int,
        username: String,
        password: String,
        email: String
    ): Flow<RegisterResponse> {
        return flow{
            //val registerResponse
            //emit(loginResponse)
        }
    }
    */
}