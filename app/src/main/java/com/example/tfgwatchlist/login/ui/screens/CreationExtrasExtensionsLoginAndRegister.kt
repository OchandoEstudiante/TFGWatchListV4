package com.example.tfgwatchlist.login.ui.screens

import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewmodel.CreationExtras
import com.example.tfgwatchlist.WatchlistApp
import com.example.tfgwatchlist.login.domain.LoginAndRegisterRepository

fun CreationExtras.getLoginAndRegisterRepository(): LoginAndRegisterRepository{
    val application = this[APPLICATION_KEY] as WatchlistApp
    return application.loginAndRegisterRepository

}