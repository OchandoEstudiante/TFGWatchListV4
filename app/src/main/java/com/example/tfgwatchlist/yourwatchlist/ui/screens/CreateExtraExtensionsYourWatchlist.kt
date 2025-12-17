package com.example.tfgwatchlist.yourwatchlist.ui.screens

import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewmodel.CreationExtras
import com.example.tfgwatchlist.WatchlistApp
import com.example.tfgwatchlist.yourwatchlist.domain.YourWatchlistRepository

fun CreationExtras.getYourWatchlistRepository(): YourWatchlistRepository{
    val application = this[APPLICATION_KEY] as WatchlistApp
    return application.yourWatchlistRepository
}