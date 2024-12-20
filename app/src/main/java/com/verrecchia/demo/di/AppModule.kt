package com.verrecchia.demo.di

import com.verrecchia.touchtunes_domain.AlbumRepository
import com.verrecchia.touchtunes_domain.SearchAlbumUseCase
import com.verrecchia.touchtunes_domain.SearchAlbumUseCaseImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object UseCaseModule {

    @Provides
    @Singleton
    fun provideSearchAlbumUseCase(
        repository: AlbumRepository
    ): SearchAlbumUseCase {
        return SearchAlbumUseCaseImpl(repository)
    }
}
