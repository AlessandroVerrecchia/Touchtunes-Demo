package com.verrecchia.demo

import com.verrecchia.demo.album.DemoViewModel
import com.verrecchia.demo.album.Event
import com.verrecchia.demo.album.Intent
import com.verrecchia.demo.album.ViewState
import com.verrecchia.touchtunes_domain.Album
import com.verrecchia.touchtunes_domain.SearchAlbumUseCase
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.*
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class DemoViewModelTest {

    private lateinit var searchAlbumUseCase: SearchAlbumUseCase
    private lateinit var viewModel: DemoViewModel

    private val testDispatcher = StandardTestDispatcher()

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        searchAlbumUseCase = mockk()
        viewModel = DemoViewModel(searchAlbumUseCase)
    }

    @Test
    fun `handleIntent with Search updates viewState to SearchResults on success`() = runTest {
        // GIVEN
        // Mock successful response
        val mockAlbums = listOf(Album("1", "Album 1"), Album("2", "Album 2"))
        coEvery { searchAlbumUseCase.execute("query") } returns flow {
            emit(Result.success(mockAlbums))
        }

        // WHEN
        viewModel.handleIntent(Intent.Search("query"))
        advanceUntilIdle()

        // THEN
        assertEquals(ViewState.SearchResults(mockAlbums), viewModel.viewState.value)
    }

    @Test
    fun `handleIntent with Search updates viewState to Error and emits Event on failure`() =
        runTest {
            // GIVEN
            // Mock failure response
            val exception = Exception("Network error")
            coEvery { searchAlbumUseCase.execute("query") } returns flow {
                emit(Result.failure(exception))
            }
            // Collect emitted events into a list
            val emittedEvents = mutableListOf<Event>()
            val job = launch {
                viewModel.event.toList(emittedEvents)
            }

            // WHEN
            // Trigger the intent
            viewModel.handleIntent(Intent.Search("query"))
            advanceUntilIdle()

            // Assert the viewState is updated
            assertEquals(
                ViewState.Error("Search failed: ${exception.message}"),
                viewModel.viewState.value
            )

            // THEN
            assert(emittedEvents.contains(Event.ShowErrorToast("Search failed: ${exception.message}")))
            job.cancel()
        }


    @Test
    fun `handleIntent with AlbumClicked emits ShowAlbumDetails event`() = runTest {
        // GIVEN
        val album = Album("1", "Album 1")
        // Collect emitted events into a list
        val emittedEvents = mutableListOf<Event>()
        val job = launch {
            viewModel.event.toList(emittedEvents)
        }

        // WHEN
        viewModel.handleIntent(Intent.AlbumClicked(album))
        advanceUntilIdle()

        // THEN
        assert(emittedEvents.contains(Event.ShowAlbumDetails(album)))
        job.cancel()
    }

    @Test
    fun `handleIntent with Retry reuses latestSearch and updates viewState`() = runTest {
        // GIVEN
        // Mock successful response
        val mockAlbums = listOf(Album("1", "Album 1"))
        coEvery { searchAlbumUseCase.execute("latestSearch") } returns flow {
            emit(Result.success(mockAlbums))
        }
        // set "latest search query"
        viewModel.handleIntent(Intent.Search("latestSearch"))
        advanceUntilIdle()

        // WHEN
        viewModel.handleIntent(Intent.Retry)
        advanceUntilIdle()

        // THEN
        assertEquals(ViewState.SearchResults(mockAlbums), viewModel.viewState.value)
    }
}
