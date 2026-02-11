package com.example.tfgwatchlist.login.data.network.providers

import com.example.tfgwatchlist.login.data.network.api.LoginAndRegisterApiService
import retrofit2.Retrofit

object LoginAndRegisterServiceProvider {
    fun provideYourLogins(retrofit: Retrofit) =
        retrofit.create(LoginAndRegisterApiService::class.java)
}