package com.verrecchia.touchtunes_domain

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class SearchAlbumUseCaseImpl @Inject constructor(
    private val repository: AlbumRepository
) : SearchAlbumUseCase {
    override fun execute(searchTerm: String): Flow<Result<List<Album>>> = flow {
        try {
            val albums = repository.searchAlbums(searchTerm)
            emit(Result.success(albums))
        } catch (e: Exception) {
            emit(Result.failure(e))
        }
    }
}