package com.verrecchia.itune_data.network

import com.verrecchia.itune_data.model.AlbumResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface ITuneApiService {

    @GET("search")
    suspend fun searchAlbumByArtistName(
        @Query("term")
        query: String,
        @Query("media")
        media: String = "music",
        @Query("entity")
        entity: String = "album",
        @Query("attribute")
        attribute: String = "artistTerm",
        @Query("country")
        country: String = "CA"
    ): AlbumResponse
}