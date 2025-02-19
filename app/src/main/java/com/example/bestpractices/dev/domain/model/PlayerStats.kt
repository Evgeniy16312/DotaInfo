package com.example.bestpractices.dev.domain.model

data class PlayerStats(
    val accountId: Long,
    val playerName: String,
    val realName: String?,
    val avatarUrl: String,
    val avatarMediumUrl: String,
    val soloCompetitiveRank: Int?,
    val competitiveRank: Int?,
    val leaderboardRank: Int?,
    val lastLogin: String?,
    val country: String?,
    val profileUrl: String?,
    val steamId: String?,
    val isDotaPlusSubscriber: Boolean,
    val cheese: Int,
    val isContributor: Boolean,
    val isSubscriber: Boolean
)
