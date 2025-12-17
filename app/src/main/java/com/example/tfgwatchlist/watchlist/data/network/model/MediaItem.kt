package com.example.tfgwatchlist.watchlist.data.network.model

sealed class MediaItem {
    abstract val id: Int
    abstract val titulo: String?
    abstract val premisa: String?
    abstract val poster: String?
    abstract val banner: String?
    abstract val fechaEstreno: String?
}