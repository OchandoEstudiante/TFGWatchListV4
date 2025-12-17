package com.example.tfgwatchlist.yourwatchlist.data.network.model

import java.io.Serializable

sealed class ItemMediaMongoItem: Serializable {
    abstract val id: Int
    abstract val nombre: String
    abstract val premisa: String?
    abstract val generos: List<GeneroMongoItem>
    abstract val imagen: String?
    abstract val banner: String?
    abstract val trailer: String?
    abstract val fechaEstreno: String?
    abstract val estado: String?
}