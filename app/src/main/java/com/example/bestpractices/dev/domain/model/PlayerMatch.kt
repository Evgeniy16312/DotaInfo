package com.example.bestpractices.dev.domain.model

data class PlayerMatch(
    val matchId: Long,
    val heroId: Int,
    val kills: Int,
    val deaths: Int,
    val assists: Int,
    val duration: Int,
    val isWin: Boolean
)