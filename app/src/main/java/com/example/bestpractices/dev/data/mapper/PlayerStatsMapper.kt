package com.example.bestpractices.dev.data.mapper

import com.example.bestpractices.dev.data.model.PlayerStatsResponse
import com.example.bestpractices.dev.domain.model.PlayerStats

class PlayerStatsMapper {
    fun mapToPlayerStats(response: PlayerStatsResponse): PlayerStats {
        return PlayerStats(
            accountId = response.profile?.accountId ?: 0,
            playerName = response.profile?.personaname ?: "Unknown",
            realName = response.profile?.name ?: "N/A",
            avatarUrl = response.profile?.avatarFull ?: "",
            avatarMediumUrl = response.profile?.avatarMedium ?: "",
            soloCompetitiveRank = response.soloCompetitiveRank,
            competitiveRank = response.competitiveRank,
            leaderboardRank = response.leaderboardRank,
            lastLogin = response.profile?.lastLogin ?: "Неизвестно",
            country = response.profile?.locCountryCode ?: "N/A",
            profileUrl = response.profile?.profileUrl ?: "",
            steamId = response.profile?.steamId ?: "",
            isDotaPlusSubscriber = response.profile?.plus ?: false,
            cheese = response.profile?.cheese ?: 0,
            isContributor = response.profile?.isContributor ?: false,
            isSubscriber = response.profile?.isSubscriber ?: false
        )
    }
}