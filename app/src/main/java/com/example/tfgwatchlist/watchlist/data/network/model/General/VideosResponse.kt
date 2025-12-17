package com.example.tfgwatchlist.watchlist.data.network.model.General

import com.squareup.moshi.Json

data class VideosResponse (
    @Json(name="results")val results: List<VideoItem>
)