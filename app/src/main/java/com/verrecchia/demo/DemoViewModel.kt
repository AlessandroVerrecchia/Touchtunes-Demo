package com.verrecchia.demo

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.verrecchia.demo.album.Event
import com.verrecchia.demo.album.Intent
import com.verrecchia.demo.album.ViewState
import com.verrecchia.touchtunes_domain.Album
import com.verrecchia.touchtunes_domain.SearchAlbumUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DemoViewModel @Inject constructor(private val searchAlbumUseCase: SearchAlbumUseCase) :
    ViewModel() {

    private val _viewState = MutableStateFlow<ViewState>(ViewState.Idle)
    val viewState: StateFlow<ViewState> = _viewState

    private val _event = MutableSharedFlow<Event>()
    val event: SharedFlow<Event> = _event

    private var latestSearch = ""

    fun handleIntent(intent: Intent) {
        when (intent) {
            is Intent.Search -> searchAlbums(intent.query)
            is Intent.AlbumClicked -> handleAlbumClick(intent.album)
            is Intent.Retry -> searchAlbums(latestSearch)
        }
    }

    private fun searchAlbums(query: String) {
        viewModelScope.launch {
            _viewState.value = ViewState.Loading
            latestSearch = query
            searchAlbumUseCase.execute(query).collect { result ->
                if (result.isSuccess) {
                    val albums = result.getOrNull()
                    _viewState.value = if (albums.isNullOrEmpty()) {
                        ViewState.Empty
                    } else {
                        ViewState.SearchResults(albums)
                    }
                }
                if (result.isFailure) {
                    val error = result.exceptionOrNull()
                    _viewState.value = ViewState.Error("Search failed: ${error?.message}")
                    _event.emit(Event.ShowErrorToast("Search failed: ${error?.message}"))
                }
            }
        }
    }

    private fun handleAlbumClick(album: Album) {
        viewModelScope.launch {
            _event.emit(Event.ShowAlbumDetails(album))
        }
    }

}