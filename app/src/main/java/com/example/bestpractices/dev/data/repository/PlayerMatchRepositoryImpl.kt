package com.example.bestpractices.dev.data.repository

import com.example.bestpractices.dev.data.api.OpenDotaApiService
import com.example.bestpractices.dev.data.database.PlayerMatchDao
import com.example.bestpractices.dev.data.mapper.PlayerMatchMapper
import com.example.bestpractices.dev.data.mapper.toDomain
import com.example.bestpractices.dev.data.mapper.toEntity
import com.example.bestpractices.dev.domain.model.PlayerMatch
import com.example.bestpractices.dev.domain.repository.PlayerMatchRepository

class PlayerMatchRepositoryImpl(
    private val apiService: OpenDotaApiService,
    private val mapper: PlayerMatchMapper,
    private val playerMatchDao: PlayerMatchDao
) : PlayerMatchRepository {

    override suspend fun getPlayerMatch(
        accountId: Long,
        page: Int,
        pageSize: Int
    ): List<PlayerMatch> {
        val offset = page * pageSize

        // Загружаем новую страницу из API
        val response = apiService.getPlayerMatches(accountId, limit = pageSize, offset = offset)
        val matches = mapper.mapToPlayerMatch(response)

        // Сохраняем новые матчи в БД (они заменят старые при совпадении matchId)
        val entities = matches.map { it.toEntity(accountId) }
        playerMatchDao.insertMatches(entities)

        // Загружаем все матчи из базы (чтобы вернуть актуальные данные)
        val cachedMatches = playerMatchDao.getMatchesByAccountId(accountId)

        return cachedMatches.map { it.toDomain() }
    }

    override suspend fun clearCache(accountId: Long) {
        playerMatchDao.clearCacheForAccount(accountId)
    }
}