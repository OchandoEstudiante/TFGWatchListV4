package com.example.tfgwatchlist.watchlist.data.network.model.General

import com.squareup.moshi.Json

data class CrewResponse (
    @Json(name="id") val id: Int,
    @Json(name="name") val name: String,
    @Json(name="job") val job: String,
    @Json(name="profile_path") val imagenPerfil: String?
)