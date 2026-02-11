package com.example.tfgwatchlist

import android.app.Application
import android.util.Log
import com.example.tfgwatchlist.core.data.network.provider.RetrofitHelper
import com.example.tfgwatchlist.core.data.network.provider.RetrofitYourWatchlist
import com.example.tfgwatchlist.login.data.network.LoginAndRegisterRepositoryImpl
import com.example.tfgwatchlist.login.data.network.providers.LoginAndRegisterServiceProvider
import com.example.tfgwatchlist.login.domain.LoginAndRegisterRepository
import com.example.tfgwatchlist.watchlist.data.network.WatchlistRepositoryImpl
import com.example.tfgwatchlist.watchlist.data.network.providers.WatchlistServiceProvider
import com.example.tfgwatchlist.watchlist.domain.WatchlistRepository
import com.example.tfgwatchlist.yourwatchlist.data.network.YourWatchlistRepositoryImpl
import com.example.tfgwatchlist.yourwatchlist.data.network.api.YourWatchlistApiService
import com.example.tfgwatchlist.yourwatchlist.data.network.providers.YourWatchlistServiceProvider
import com.example.tfgwatchlist.yourwatchlist.domain.YourWatchlistRepository


class WatchlistApp: Application() {
    lateinit var watchlistRepository: WatchlistRepository

    lateinit var yourWatchlistRepository: YourWatchlistRepository

    lateinit var loginAndRegisterRepository: LoginAndRegisterRepository

    override fun onCreate() {
        super.onCreate()
        val retrofit = RetrofitHelper.provideRetrofit()
        Log.i("Retrofit de TMDB","Retrofit:${retrofit}")

        val yourWatchlistRetrofit = RetrofitYourWatchlist.provideRetrofit()
        Log.i("Retrofit de mongo", "Your Retrofit:${yourWatchlistRetrofit}")

        val watchlistApiService = WatchlistServiceProvider.provideWatchlist(retrofit)
        Log.i("Api services de TMDB", "WatchlistApiService:${watchlistApiService}")

        val yourWatchlistApiService = YourWatchlistServiceProvider.provideYourWatchlist(yourWatchlistRetrofit)
        Log.i("Api service de mongo", "${yourWatchlistApiService}")

        val loginAndRegisterApiService = LoginAndRegisterServiceProvider.provideYourLogins(yourWatchlistRetrofit)
        //val loginsApiService = YourWatchlistServiceProvider.provideYourWatchlist(yourWatchlistRetrofit)
        //Log.i("Api service de mongo para users", "${loginsApiService}")

        watchlistRepository = WatchlistRepositoryImpl(watchlistApiService)
        Log.i("Respositorio de TMDB", "WatchlistRepository: ${watchlistRepository}")

        //Ver como arreglar esto
        yourWatchlistRepository = YourWatchlistRepositoryImpl(yourWatchlistApiService)
        Log.i("Repositorio de Mongo", "${yourWatchlistRepository}")

        loginAndRegisterRepository = LoginAndRegisterRepositoryImpl(loginAndRegisterApiService)
        Log.i("Repositorio de Logins y registros", "${yourWatchlistRepository}")

        //yourWatchlistAndLoginApiService
    }
}