package com.example.tfgwatchlist.yourwatchlist.domain

enum class MediaStatus(val key: String) {
    ALL("Todas"),
    WATCHING("Viendo"),
    WATCHED("Vista"),
    HALF_WATCHED("A medias"),
    TO_BE_SEEN("Por ver")
}