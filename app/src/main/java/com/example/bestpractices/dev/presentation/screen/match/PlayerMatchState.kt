package com.example.bestpractices.dev.presentation.screen.match

import com.example.bestpractices.dev.domain.model.PlayerMatch

sealed class PlayerMatchState {
    data object Idle : PlayerMatchState()
    data object Loading : PlayerMatchState()
    data class Success(val matches: List<PlayerMatch>) : PlayerMatchState()
    data class Error(val message: String) : PlayerMatchState()
}