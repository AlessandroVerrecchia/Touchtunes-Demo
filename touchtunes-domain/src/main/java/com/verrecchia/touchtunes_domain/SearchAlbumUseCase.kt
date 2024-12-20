package com.verrecchia.touchtunes_domain

import kotlinx.coroutines.flow.Flow

interface SearchAlbumUseCase {
    fun execute(searchTerm: String): Flow<Result<List<Album>>>
}

