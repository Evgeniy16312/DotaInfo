package com.example.bestpractices.dev.data.repository

import com.example.bestpractices.dev.data.api.OpenDotaApiService
import com.example.bestpractices.dev.data.database.hero.HeroDao
import com.example.bestpractices.dev.data.mapper.HeroesMapper
import com.example.bestpractices.dev.domain.model.Heroes
import com.example.bestpractices.dev.domain.repository.HeroesRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class HeroesRepositoryImpl @Inject constructor(
    private val apiService: OpenDotaApiService,
    private val heroDao: HeroDao,
    private val mapper: HeroesMapper
) : HeroesRepository {
    override suspend fun getHeroes(): List<Heroes> = withContext(Dispatchers.IO) {
        val localHeroes = heroDao.getAllHeroes()
        if (localHeroes.isNotEmpty()) {
            return@withContext mapper.mapEntityToDomain(localHeroes)
        }

        val heroesResponse = apiService.getHeroes()
        val heroes = mapper.mapResponseToDomain(heroesResponse)
        heroDao.insertHeroes(mapper.mapDomainToEntity(heroes))
        heroes
    }

    override suspend fun updateHeroes(): List<Heroes> = withContext(Dispatchers.IO) {
        val heroesResponse = apiService.getHeroes()
        val heroes = mapper.mapResponseToDomain(heroesResponse)
        heroDao.clearHeroes()
        heroDao.insertHeroes(mapper.mapDomainToEntity(heroes))
        heroes
    }
}