package com.example.tfgwatchlist.yourwatchlist.data.network.model

import com.squareup.moshi.Json

data class EpisodioMongoItem (
    @Json(name="nombreEpisodio") val nombreEpisodio: String,
    @Json(name="premisaEpisodio") val premisaEpisodio: String,
    @Json(name="numeroEpisodio") val numeroEpisodio: String,
    @Json(name="imagenEpisodio") val imagenEpisodio: String?,
    @Json(name="fecha_emision") val fecha_emision: String?,
    @Json(name="duracion") val duracion: Int?,
)