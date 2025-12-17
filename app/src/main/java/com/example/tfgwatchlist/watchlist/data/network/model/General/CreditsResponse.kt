package com.example.tfgwatchlist.watchlist.data.network.model.General

import com.squareup.moshi.Json

data class CreditsResponse (
    //@Json(name="crew") val crew: List<CrewResponse>,
    @Json(name="cast") val cast: List<CastResponse>
)