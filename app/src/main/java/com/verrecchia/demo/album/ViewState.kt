package com.verrecchia.demo.album

import com.verrecchia.touchtunes_domain.Album

sealed class ViewState {
    data object Idle : ViewState()
    data object Loading : ViewState()
    data class Error(val message: String) : ViewState()
    data object Empty : ViewState()
    data class SearchResults(val albums: List<Album>) : ViewState()
}