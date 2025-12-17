package com.example.tfgwatchlist.watchlist.data.network.model

import com.squareup.moshi.Json

data class PeliculaResponse (
    @Json(name="id") override val id: Int,
    @Json(name = "title") override val titulo: String? = null,
    @Json(name="poster_path")override val poster: String? = null,
    @Json(name="overview")override val premisa: String? = null,
    @Json(name = "backdrop_path") override val banner: String? = null,
    @Json(name = "release_date") override val fechaEstreno: String? = null
) : MediaItem()