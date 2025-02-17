package com.example.bestpractices.dev.data.repository

import com.example.bestpractices.dev.data.api.OpenDotaApiService
import com.example.bestpractices.dev.data.mapper.PlayerMatchMapper
import com.example.bestpractices.dev.domain.model.PlayerMatch
import com.example.bestpractices.dev.domain.repository.PlayerMatchRepository

class PlayerMatchRepositoryImpl (
    private val apiService: OpenDotaApiService,
    private val mapper: PlayerMatchMapper
    ) : PlayerMatchRepository {

    override suspend fun getPlayerMatch(accountId: Long): List<PlayerMatch> {
        val response = apiService.getPlayerMatches(accountId)
        return mapper.mapToPlayerMatch(response)
    }
}