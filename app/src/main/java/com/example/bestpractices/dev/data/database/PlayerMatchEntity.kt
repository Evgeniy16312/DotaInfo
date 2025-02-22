package com.example.bestpractices.dev.data.database

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "player_matches")
data class PlayerMatchEntity(
    @PrimaryKey val matchId: Long,
    val heroId: Int,
    val kills: Int,
    val deaths: Int,
    val assists: Int,
    val duration: Int,
    val isWin: Boolean,
    val accountId: Long
)