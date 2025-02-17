package com.example.bestpractices.dev.data.repository

import com.example.bestpractices.dev.data.api.OpenDotaApiService
import com.example.bestpractices.dev.data.mapper.PlayerStatsMapper
import com.example.bestpractices.dev.domain.model.PlayerStats
import com.example.bestpractices.dev.domain.repository.PlayerStatsRepository
import javax.inject.Inject


class PlayerStatsRepositoryImpl @Inject constructor(
    private val apiService: OpenDotaApiService,
    private val mapper: PlayerStatsMapper
) : PlayerStatsRepository {

    override suspend fun getPlayerStats(accountId: Long): PlayerStats {
        val response = apiService.getPlayerStats(accountId)
        return mapper.mapToPlayerStats(response)
    }
}