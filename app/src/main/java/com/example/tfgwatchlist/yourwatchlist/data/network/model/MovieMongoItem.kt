package com.example.tfgwatchlist.yourwatchlist.data.network.model

import com.squareup.moshi.Json
import java.io.Serializable

data class MovieMongoItem (
    @Json(name="_id") override val id: Int,
    @Json(name="nombre") override val nombre: String,
    @Json(name="premisa") override val premisa: String?,
    @Json(name="generos") override val generos: List<GeneroMongoItem>,
    @Json(name="imagen") override val imagen: String?,
    @Json(name="banner") override val banner: String?,
    @Json(name="fechaEstreno") override val fechaEstreno: String?,
    @Json(name="duracion") val duracion: Int,
    @Json(name="trailer") override val trailer: String?,
    @Json(name="estado") override val estado: String?,
    ):ItemMediaMongoItem(), Serializable