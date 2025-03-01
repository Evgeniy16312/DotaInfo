package com.example.bestpractices.dev.data.database.matchs

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface PlayerMatchDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMatches(matches: List<PlayerMatchEntity>)

    @Query("SELECT * FROM player_matches WHERE accountId = :accountId ORDER BY matchId DESC")
    suspend fun getMatchesByAccountId(accountId: Long): List<PlayerMatchEntity>

    @Query("DELETE FROM player_matches WHERE accountId = :accountId")
    suspend fun clearCacheForAccount(accountId: Long)
}