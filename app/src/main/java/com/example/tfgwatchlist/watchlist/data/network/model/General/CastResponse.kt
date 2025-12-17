package com.example.tfgwatchlist.watchlist.data.network.model.General

import com.squareup.moshi.Json

data class CastResponse (
    @Json(name="id") val id: Int,
    @Json(name="name") val name: String,
    @Json(name="character") val character: String,
    @Json(name="profile_path") val imagenPerfil: String?
    )