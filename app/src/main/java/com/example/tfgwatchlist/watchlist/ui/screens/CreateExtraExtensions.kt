package com.example.tfgwatchlist.watchlist.ui.screens

import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewmodel.CreationExtras
import com.example.tfgwatchlist.WatchlistApp
import com.example.tfgwatchlist.watchlist.domain.WatchlistRepository

fun CreationExtras.getWatchlistRepository(): WatchlistRepository{
    val application = this[APPLICATION_KEY] as WatchlistApp
    return application.watchlistRepository
}