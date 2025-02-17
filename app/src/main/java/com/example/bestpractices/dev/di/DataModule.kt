package com.example.bestpractices.dev.di

import com.example.bestpractices.dev.data.api.OpenDotaApiService
import com.example.bestpractices.dev.data.api.RetrofitClient
import com.example.bestpractices.dev.data.mapper.PlayerMatchMapper
import com.example.bestpractices.dev.data.mapper.PlayerStatsMapper
import com.example.bestpractices.dev.data.repository.PlayerMatchRepositoryImpl
import com.example.bestpractices.dev.data.repository.PlayerStatsRepositoryImpl
import com.example.bestpractices.dev.domain.repository.PlayerMatchRepository
import com.example.bestpractices.dev.domain.repository.PlayerStatsRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DataModule {

    @Provides
    @Singleton
    fun provideOpenDotaApiService(): OpenDotaApiService = RetrofitClient.create()

    @Provides
    @Singleton
    fun providePlayerStatsMapper(): PlayerStatsMapper = PlayerStatsMapper()

    @Provides
    @Singleton
    fun providePlayerMatchMapper(): PlayerMatchMapper = PlayerMatchMapper()

    @Provides
    @Singleton
    fun providePlayerStatsRepository(
        apiService: OpenDotaApiService,
        mapper: PlayerStatsMapper
    ): PlayerStatsRepository = PlayerStatsRepositoryImpl(apiService, mapper)

    @Provides
    @Singleton
    fun providePlayerMatchRepository(
        apiService: OpenDotaApiService,
        mapper: PlayerMatchMapper
    ): PlayerMatchRepository = PlayerMatchRepositoryImpl(apiService, mapper)
}