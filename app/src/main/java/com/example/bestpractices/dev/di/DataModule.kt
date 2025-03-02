package com.example.bestpractices.dev.di

import android.content.Context
import com.example.bestpractices.dev.data.api.OpenDotaApiService
import com.example.bestpractices.dev.data.api.RetrofitClient
import com.example.bestpractices.dev.data.database.AppDatabase
import com.example.bestpractices.dev.data.database.hero.HeroDao
import com.example.bestpractices.dev.data.database.matchs.PlayerMatchDao
import com.example.bestpractices.dev.data.mapper.HeroesMapper
import com.example.bestpractices.dev.data.mapper.PlayerMatchMapper
import com.example.bestpractices.dev.data.mapper.PlayerStatsMapper
import com.example.bestpractices.dev.data.repository.HeroesRepositoryImpl
import com.example.bestpractices.dev.data.repository.PlayerMatchRepositoryImpl
import com.example.bestpractices.dev.data.repository.PlayerStatsRepositoryImpl
import com.example.bestpractices.dev.domain.repository.HeroesRepository
import com.example.bestpractices.dev.domain.repository.PlayerMatchRepository
import com.example.bestpractices.dev.domain.repository.PlayerStatsRepository
import com.example.bestpractices.dev.presentation.screen.PreferencesManager
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DataModule {

    @Provides
    @Singleton
    fun providePreferencesManager(@ApplicationContext context: Context): PreferencesManager {
        return PreferencesManager(context)
    }

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
    fun provideHeroesMapper(): HeroesMapper = HeroesMapper()

    @Provides
    @Singleton
    fun providePlayerStatsRepository(
        apiService: OpenDotaApiService,
        mapper: PlayerStatsMapper
    ): PlayerStatsRepository = PlayerStatsRepositoryImpl(apiService, mapper)

    @Provides
    @Singleton
    fun provideAppDatabase(@ApplicationContext context: Context): AppDatabase {
        return AppDatabase.getDatabase(context)
    }

    @Provides
    @Singleton
    fun providePlayerMatchDao(database: AppDatabase): PlayerMatchDao {
        return database.playerMatchDao()
    }

    @Provides
    @Singleton
    fun providePlayerMatchRepository(
        apiService: OpenDotaApiService,
        mapper: PlayerMatchMapper,
        playerMatchDao: PlayerMatchDao
    ): PlayerMatchRepository {
        return PlayerMatchRepositoryImpl(apiService, mapper, playerMatchDao)
    }

    @Provides
    @Singleton
    fun provideHeroDao(database: AppDatabase): HeroDao {
        return database.heroDao()
    }

    @Provides
    @Singleton
    fun provideHeroesRepository(
        apiService: OpenDotaApiService,
        heroDao: HeroDao,
        mapper: HeroesMapper
    ): HeroesRepository = HeroesRepositoryImpl(apiService, heroDao, mapper)
}