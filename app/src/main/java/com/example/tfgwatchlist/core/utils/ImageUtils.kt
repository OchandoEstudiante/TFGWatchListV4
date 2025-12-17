package com.example.tfgwatchlist.core.utils

fun generarURL(extra: String):String{
    return "https://image.tmdb.org/t/p/${extra}"
}

fun generarURLYoutube(extra: String): String{
    return "https://img.youtube.com/vi/${extra}/hqdefault.jpg"
}
