package com.example.tfgwatchlist.watchlist.data.network.model.General

import com.squareup.moshi.Json

data class VideoItem (
    @Json(name="key") val key: String?,
)