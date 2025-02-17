package com.example.bestpractices.dev.domain.repository

import com.example.bestpractices.dev.domain.model.PlayerStats

interface PlayerStatsRepository {
    suspend fun getPlayerStats(accountId: Long): PlayerStats
}