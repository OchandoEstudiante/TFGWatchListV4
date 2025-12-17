package com.example.tfgwatchlist.yourwatchlist.data.network.model

import com.squareup.moshi.Json
import java.io.Serializable

data class GeneroMongoItem (
    @Json(name="nombre") val nombre: String,
): Serializable