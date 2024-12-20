package com.verrecchia.demo.album

import com.verrecchia.touchtunes_domain.Album

sealed class Event {
    data class ShowErrorToast(val message: String) : Event()
    data class ShowAlbumDetails(val album: Album) : Event()
}