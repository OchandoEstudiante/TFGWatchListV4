package com.example.tfgwatchlist.yourwatchlist.data.network.model

import com.squareup.moshi.Json
import java.io.Serializable

data class SerieMongoItem (
    @Json(name="_id") override val id: Int,
    @Json(name="nombre") override val nombre: String,
    @Json(name="premisa") override val premisa: String?,
    @Json(name="generos") override val generos: List<GeneroMongoItem>,
    @Json(name="numeroTemporadas") val numeroTemporadas: Int,
    @Json(name="imagen") override val imagen: String?,
    @Json(name="banner") override val banner: String?,
    @Json(name="fechaPrimeraEmision") override val fechaEstreno: String?,
    @Json(name="trailer") override val trailer: String?,
    @Json(name="estado") override val estado: String?
): ItemMediaMongoItem(), Serializable