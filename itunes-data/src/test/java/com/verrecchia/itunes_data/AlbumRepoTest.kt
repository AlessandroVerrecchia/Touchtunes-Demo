package com.verrecchia.itunes_data

import com.verrecchia.itunes_data.model.AlbumDto
import com.verrecchia.itunes_data.model.AlbumResponse
import com.verrecchia.itunes_data.network.ITuneAlbumRepository
import com.verrecchia.itunes_data.network.ITuneApiService
import com.verrecchia.touchtunes_domain.Album
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class ITuneAlbumRepositoryTest {

    private lateinit var apiService: ITuneApiService
    private lateinit var albumRepository: ITuneAlbumRepository

    @Before
    fun setUp() {
        apiService = mockk()
        albumRepository = ITuneAlbumRepository(apiService)
    }

    @Test
    fun `searchAlbums should return a list of albums when API response is successful`() = runBlocking {
        // GIVEN
        // Mock response from the API
        val mockApiResponse = AlbumResponse(
            resultCount = 2,
            results = listOf(
                AlbumDto(
                    collectionId = 123456789,
                    collectionName = "In Between Dreams",
                    artistName = "Jack Johnson",
                    artworkUrl100 = "https://example.com/artwork.jpg",
                    releaseDate = "2005-02-28T08:00:00Z",
                    primaryGenreName = "Rock",
                    currency = "USD",
                    collectionPrice = 9.99,
                    copyright = "℗ 2005 Jack Johnson"
                ),
                AlbumDto(
                    collectionId = 987654321,
                    collectionName = "Brushfire Fairytales",
                    artistName = "Jack Johnson",
                    artworkUrl100 = "https://example.com/artwork2.jpg",
                    releaseDate = "2001-02-01T08:00:00Z",
                    primaryGenreName = "Folk",
                    currency = "USD",
                    collectionPrice = 8.99,
                    copyright = "℗ 2001 Jack Johnson"
                )
            )
        )
        // Mock API behavior
        coEvery { apiService.searchAlbumByArtistName("Jack Johnson") } returns mockApiResponse

        // WHEN
        // Call the repository method
        val result = albumRepository.searchAlbums("Jack Johnson")
        // Expected result
        val expected = listOf(
            Album(
                id = "123456789",
                title = "In Between Dreams",
                genre = "Rock",
                price = 9.99,
                currency = "USD",
                copyright = "℗ 2005 Jack Johnson",
                releaseDate = "February 28, 2005", // Assuming `toReadableFormat()` formats the date
                artwork = "https://example.com/artwork.jpg"
            ),
            Album(
                id = "987654321",
                title = "Brushfire Fairytales",
                genre = "Folk",
                price = 8.99,
                currency = "USD",
                copyright = "℗ 2001 Jack Johnson",
                releaseDate = "February 01, 2001", // Assuming `toReadableFormat()` formats the date
                artwork = "https://example.com/artwork2.jpg"
            )
        )
        // THEN
        assertEquals(expected, result)
    }
}

