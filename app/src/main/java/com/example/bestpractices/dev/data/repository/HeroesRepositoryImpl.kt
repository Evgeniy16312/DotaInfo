package com.example.bestpractices.dev.data.repository

import com.example.bestpractices.dev.data.api.OpenDotaApiService
import com.example.bestpractices.dev.data.mapper.HeroesMapper
import com.example.bestpractices.dev.domain.model.Heroes
import com.example.bestpractices.dev.domain.repository.HeroesRepository
import javax.inject.Inject

class HeroesRepositoryImpl @Inject constructor(
    private val apiService: OpenDotaApiService,
    private val mapper: HeroesMapper
) : HeroesRepository {
    override suspend fun getHeroes(): List<Heroes> {
        val heroesResponse = apiService.getHeroes()
        return mapper.mapResponseToDomain(heroesResponse)
    }
}