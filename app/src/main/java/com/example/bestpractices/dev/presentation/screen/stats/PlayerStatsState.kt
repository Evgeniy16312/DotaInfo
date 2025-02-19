package com.example.bestpractices.dev.presentation.screen.stats

import com.example.bestpractices.dev.domain.model.PlayerStats

sealed class PlayerStatsState {
    object Idle : PlayerStatsState()
    object Loading : PlayerStatsState()
    data class Success(val playerStats: PlayerStats) : PlayerStatsState()
    data class Error(val message: String) : PlayerStatsState()
}