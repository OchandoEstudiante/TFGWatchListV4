package com.example.tfgwatchlist.yourwatchlist.ui.model

import com.example.tfgwatchlist.yourwatchlist.domain.MediaStatus

data class SpinnerItemUi(
    val status: MediaStatus,
    val label: String
){
    override fun toString(): String = label
}