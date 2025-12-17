package com.example.tfgwatchlist.watchlist.data.network.model

import com.squareup.moshi.Json

data class GenerosMediaItemDetails (
    @Json(name="id") val id: Int,
    @Json(name="name") val name: String,
)