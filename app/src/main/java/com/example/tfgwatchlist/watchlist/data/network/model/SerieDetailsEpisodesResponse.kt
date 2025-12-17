package com.example.tfgwatchlist.watchlist.data.network.model

import com.squareup.moshi.Json

data class SerieDetailsEpisodesResponse (
    @Json(name="id") val id: Int,
    @Json(name="name") val nombre_episodio: String,
    @Json(name="episode_number") val numero_episodio: Int,
    @Json(name="season_number") val numero_temporada: Int,
    @Json(name="overview") val premisa_episodio: String?,
    @Json(name="still_path") val imagen_episodio: String?,
    @Json(name="air_date") val fecha_emision: String?,
    @Json(name="runtime") val duracion: String?
)