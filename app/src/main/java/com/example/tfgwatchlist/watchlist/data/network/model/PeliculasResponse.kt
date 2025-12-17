package com.example.tfgwatchlist.watchlist.data.network.model

import com.squareup.moshi.Json

data class PeliculasResponse (
    @Json(name="page") val page: Int,
    @Json(name="results") val results: List<PeliculaResponse> = listOf()
)