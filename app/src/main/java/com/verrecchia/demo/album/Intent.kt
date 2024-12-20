package com.verrecchia.demo.album

import com.verrecchia.touchtunes_domain.Album

sealed class Intent {
    data class Search(val query: String) : Intent()
    data object Retry : Intent()
    data class AlbumClicked(val album: Album) : Intent()
}