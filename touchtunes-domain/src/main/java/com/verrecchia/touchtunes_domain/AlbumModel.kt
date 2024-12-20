package com.verrecchia.touchtunes_domain

data class Album(
    val id: String = "",
    val title: String = "",
    val genre: String = "",
    val price: Double = 0.0,
    val currency: String = "",
    val copyright: String = "",
    val artwork: String = "",
    val releaseDate: String = ""
)
