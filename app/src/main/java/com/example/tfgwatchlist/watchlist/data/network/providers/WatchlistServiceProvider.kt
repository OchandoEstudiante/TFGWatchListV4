package com.example.tfgwatchlist.watchlist.data.network.providers

import com.example.tfgwatchlist.watchlist.data.network.api.WatchlistApiService
import retrofit2.Retrofit

object WatchlistServiceProvider {

    fun provideWatchlist(retrofit: Retrofit) =
        retrofit.create(WatchlistApiService::class.java)
}