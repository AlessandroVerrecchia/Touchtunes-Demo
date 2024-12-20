package com.verrecchia.itune_data

import com.verrecchia.itune_data.model.AlbumDto
import com.verrecchia.itune_data.model.toAlbum
import com.verrecchia.touchtunes_domain.Album
import org.junit.Test

import org.junit.Assert.*

class MapperTest {

    @Test
    fun `test albumDto mapping`() {
        // GIVEN
        val albumDto = AlbumDto(
            collectionId = 420L,
            artistName = "Santa Claus",
            artworkUrl100 = "Nicol Bolas.png",
            releaseDate = "2005-02-28T08:00:00Z",
            primaryGenreName = "Funk",
            currency = "USD",
            collectionName = "D12 remix",
            collectionPrice = 4.20,
            copyright = "Club Penguin"
        )
        val expectedAlbum = Album(
            id = "420",
            title = "D12 remix",
            genre = "Funk",
            price = 4.20,
            currency = "USD",
            copyright = "Club Penguin",
            artwork = "Nicol Bolas.png",
            releaseDate = "February 28, 2005"

        )

        // WHEN
        val album = albumDto.toAlbum()

        // THEN
        assertEquals(album, expectedAlbum)
    }

    @Test
    fun `test albumDto invalid date mapping`() {
        // GIVEN
        val albumDto = AlbumDto(
            collectionId = 420L,
            artistName = "Santa Claus",
            artworkUrl100 = "Nicol Bolas.png",
            releaseDate = "the time is up now !",
            primaryGenreName = "Funk",
            currency = "USD",
            collectionName = "D12 remix",
            collectionPrice = 4.20,
            copyright = "Club Penguin"
        )
        val expectedAlbum = Album(
            id = "420",
            title = "D12 remix",
            genre = "Funk",
            price = 4.20,
            currency = "USD",
            copyright = "Club Penguin",
            artwork = "Nicol Bolas.png",
            releaseDate = "Unknown Date"

        )

        // WHEN
        val album = albumDto.toAlbum()

        // THEN
        assertEquals(album, expectedAlbum)
    }
}
