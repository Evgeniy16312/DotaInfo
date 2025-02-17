package com.example.bestpractices.dev.domain.model

data class PlayerStats(
    val accountId: Long,
    val playerName: String,
    val rankTier: Int,
    val avatarUrl: String
)