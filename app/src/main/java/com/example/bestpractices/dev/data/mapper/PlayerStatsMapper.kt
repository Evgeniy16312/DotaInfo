package com.example.bestpractices.dev.data.mapper

import com.example.bestpractices.dev.data.model.PlayerStatsResponse
import com.example.bestpractices.dev.domain.model.PlayerStats

class PlayerStatsMapper {

    fun mapToPlayerStats(playerStatsResponse: PlayerStatsResponse): PlayerStats {
        return PlayerStats(
            accountId = playerStatsResponse.profile?.accountId ?: 0,
            playerName = playerStatsResponse.profile?.personaname ?: "Unknown",
            rankTier = playerStatsResponse.rankTier ?: 0,
            avatarUrl = playerStatsResponse.profile?.avatarFull ?: ""
        )
    }
}