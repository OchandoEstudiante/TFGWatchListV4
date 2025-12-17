package com.example.tfgwatchlist.watchlist.data.network.model

import com.example.tfgwatchlist.watchlist.data.network.model.General.CastResponse
import com.example.tfgwatchlist.watchlist.data.network.model.General.CreditsResponse
import com.example.tfgwatchlist.watchlist.data.network.model.General.VideosResponse

sealed class MediaItemDetails {
    abstract val id: Int
    abstract val titulo: String?
    abstract val premisa: String?
    abstract val poster: String?
    abstract val banner: String?
    abstract val fechaEstreno: String?
    abstract val generos: List<GenerosMediaItemDetails>
    abstract val credits: CreditsResponse?
    abstract val videos: VideosResponse?
}