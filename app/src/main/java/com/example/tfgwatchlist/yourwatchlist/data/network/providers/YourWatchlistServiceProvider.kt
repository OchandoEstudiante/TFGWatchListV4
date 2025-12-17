package com.example.tfgwatchlist.yourwatchlist.data.network.providers

import com.example.tfgwatchlist.yourwatchlist.data.network.api.YourWatchlistApiService
import com.example.tfgwatchlist.yourwatchlist.ui.views.YourWatchlist.YourWatchlistFragment
import retrofit2.Retrofit

object YourWatchlistServiceProvider {

    fun provideYourWatchlist(retrofit: Retrofit) =
        retrofit.create(YourWatchlistApiService::class.java)

}