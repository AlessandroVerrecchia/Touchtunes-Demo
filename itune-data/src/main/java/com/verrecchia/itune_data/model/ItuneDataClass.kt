package com.verrecchia.itune_data.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class AlbumResponse(
    @SerialName("resultCount") val resultCount: Int,
    @SerialName("results") val results: List<AlbumDto>
)

@Serializable
data class AlbumDto(
    @SerialName("collectionId") val collectionId: Long,
    @SerialName("collectionName") val collectionName: String,
    @SerialName("artistName") val artistName: String,
    @SerialName("artworkUrl100") val artworkUrl100: String,
    @SerialName("releaseDate") val releaseDate: String,
    @SerialName("primaryGenreName") val primaryGenreName: String,
    @SerialName("currency") val currency: String,
    @SerialName("collectionPrice") val collectionPrice: Double = 0.0,
    @SerialName("copyright") val copyright: String = ""
)
