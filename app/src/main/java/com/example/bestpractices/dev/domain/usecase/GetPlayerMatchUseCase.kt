package com.example.bestpractices.dev.domain.usecase

import com.example.bestpractices.dev.domain.model.PlayerMatch
import com.example.bestpractices.dev.domain.repository.PlayerMatchRepository
import javax.inject.Inject

class GetPlayerMatchUseCase @Inject constructor(
    private val repository: PlayerMatchRepository
) {
    suspend operator fun invoke(accountId: Long, page: Int, pageSize: Int): List<PlayerMatch> {
        return repository.getPlayerMatch(accountId, page, pageSize)
    }
}