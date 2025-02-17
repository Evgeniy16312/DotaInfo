package com.example.bestpractices.dev.domain.usecase

import com.example.bestpractices.dev.domain.model.PlayerStats
import com.example.bestpractices.dev.domain.repository.PlayerStatsRepository
import javax.inject.Inject

class GetPlayerStatsUseCase @Inject constructor(
    private val repository: PlayerStatsRepository
) {
    suspend operator fun invoke(accountId: Long): PlayerStats {
        return repository.getPlayerStats(accountId)
    }
}