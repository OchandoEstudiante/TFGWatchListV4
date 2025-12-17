package com.example.tfgwatchlist.yourwatchlist.data.network.model

import com.squareup.moshi.Json

data class TemporadaMongoItem(
    @Json(name="_id") val id: Int,
    @Json(name="nombreTemporada") val nombreTemporada: String,
    @Json(name="premisaTemporada") val premisaTemporada: String?,
    @Json(name="episodios") val episodios: List<EpisodioMongoItem>?,
    @Json(name="numeroTemporada") val numeroTemporada: Int,
    @Json(name="poster_temporada") val posterTemporada: String?,
    @Json(name="fecha") val fecha: String?,
    @Json(name="serieId") val serieId: Int
)
