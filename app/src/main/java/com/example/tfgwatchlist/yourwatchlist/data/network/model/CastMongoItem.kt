package com.example.tfgwatchlist.yourwatchlist.data.network.model
import com.squareup.moshi.Json

data class CastMongoItem (
    @Json(name="_id") val id: Int,
    @Json(name="nombre") val nombre: String,
    @Json(name="imagen") val imagen: String?,
    @Json(name="personajeInterpretado") val personajeInterpretado: String
)