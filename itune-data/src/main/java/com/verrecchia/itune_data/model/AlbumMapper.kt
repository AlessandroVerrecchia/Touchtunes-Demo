package com.verrecchia.itune_data.model

import android.annotation.SuppressLint
import com.verrecchia.touchtunes_domain.Album
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.Locale

fun AlbumDto.toAlbum() : Album {
    return Album(
        id = this.collectionId.toString(),
        title = this.collectionName,
        genre = this.primaryGenreName,
        price = this.collectionPrice,
        currency = this.currency,
        copyright = this.copyright,
        releaseDate = this.releaseDate.toReadableFormat(),
        artwork = this.artworkUrl100
    )
}

@SuppressLint("NewApi")
fun String.toReadableFormat(): String {
    return try {
        val instant = Instant.parse(this)
        val formatter = DateTimeFormatter.ofPattern("MMMM dd, yyyy", Locale.getDefault())
        formatter.format(instant.atZone(ZoneId.systemDefault()).toLocalDate())
    } catch (e: Exception) {
        "Unknown Date"
    }
}