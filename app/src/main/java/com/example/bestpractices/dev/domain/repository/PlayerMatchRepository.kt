package com.example.bestpractices.dev.domain.repository

import com.example.bestpractices.dev.domain.model.PlayerMatch

interface PlayerMatchRepository {
    suspend fun getPlayerMatch(accountId: Long, page: Int, pageSize: Int): List<PlayerMatch>
    suspend fun clearCache(accountId: Long)
}