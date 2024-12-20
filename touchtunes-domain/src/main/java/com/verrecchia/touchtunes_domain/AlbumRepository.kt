package com.verrecchia.touchtunes_domain

interface AlbumRepository {
    suspend fun searchAlbums(query: String): List<Album>
}