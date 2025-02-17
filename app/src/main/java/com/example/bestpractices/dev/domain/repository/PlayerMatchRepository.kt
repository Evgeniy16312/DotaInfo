package com.example.bestpractices.dev.domain.repository

import com.example.bestpractices.dev.domain.model.PlayerMatch

interface PlayerMatchRepository {
    suspend fun getPlayerMatch(accountId: Long): List<PlayerMatch>
}