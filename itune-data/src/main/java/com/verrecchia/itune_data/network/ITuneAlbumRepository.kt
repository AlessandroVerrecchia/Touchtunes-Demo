package com.verrecchia.itune_data.network

import com.verrecchia.itune_data.model.toAlbum
import com.verrecchia.touchtunes_domain.Album
import com.verrecchia.touchtunes_domain.AlbumRepository
import javax.inject.Inject


class ITuneAlbumRepository @Inject constructor(private val apiService: ITuneApiService) :
    AlbumRepository {
    override suspend fun searchAlbums(query: String): List<Album> {
        val response = apiService.searchAlbumByArtistName(query)
        return response.results.map { it.toAlbum() }
    }
}