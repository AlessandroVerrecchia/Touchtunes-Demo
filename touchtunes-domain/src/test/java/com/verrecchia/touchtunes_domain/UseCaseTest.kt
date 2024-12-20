package com.verrecchia.touchtunes_domain

import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import java.io.IOException

class SearchAlbumUseCaseImplTest {

    private lateinit var repository: AlbumRepository
    private lateinit var useCase: SearchAlbumUseCaseImpl

    @Before
    fun setUp() {
        repository = mockk()
        useCase = SearchAlbumUseCaseImpl(repository)
    }

    @Test
    fun `execute should return a successful result with a list of albums`() = runBlocking {
        // GIVEN
        // Mock response
        val mockAlbums = listOf(
            Album(
                id = "123456789",
                title = "In Between Dreams",
                genre = "Rock",
                price = 9.99,
                currency = "USD",
                copyright = "℗ 2005 Jack Johnson",
                releaseDate = "February 28, 2005",
                artwork = "https://example.com/artwork.jpg"
            )
        )
        // Mock repository behavior
        coEvery { repository.searchAlbums("Jack Johnson") } returns mockAlbums

        // WHEN
        // Execute the use case
        val result = useCase.execute("Jack Johnson").first()

        // THEN
        assert(result.isSuccess)
        assertEquals(mockAlbums, result.getOrNull())
    }

    @Test
    fun `execute should return a failure result when an exception occurs`() = runBlocking {
        // GIVEN
        // Mock repository behavior to throw an exception
        val exception = IOException("Network error")
        coEvery { repository.searchAlbums("Jack Johnson") } throws exception

        // WHEN
        val result = useCase.execute("Jack Johnson").first()

        // THEN
        assert(result.isFailure)
        assertEquals(exception, result.exceptionOrNull())
    }
}
