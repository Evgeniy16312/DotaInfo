package com.example.bestpractices.dev.data.mapper

import com.example.bestpractices.dev.data.database.PlayerMatchEntity
import com.example.bestpractices.dev.data.model.PlayerMatchResponse
import com.example.bestpractices.dev.domain.model.PlayerMatch

class PlayerMatchMapper {

    fun mapToPlayerMatch(playerMatchResponses: List<PlayerMatchResponse>): List<PlayerMatch> {
        return playerMatchResponses.map { response ->
            val isPlayerRadiant = response.playerSlot in 0..127
            val isWin =
                (isPlayerRadiant && response.radiantWin) || (!isPlayerRadiant && !response.radiantWin)

            PlayerMatch(
                matchId = response.matchId,
                heroId = response.heroId,
                kills = response.kills,
                deaths = response.deaths,
                assists = response.assists,
                duration = response.duration,
                isWin = isWin
            )
        }
    }
}

// Маппинг из Entity (БД) в Domain
fun PlayerMatchEntity.toDomain(): PlayerMatch {
    return PlayerMatch(
        matchId = this.matchId,
        heroId = this.heroId,
        kills = this.kills,
        deaths = this.deaths,
        assists = this.assists,
        duration = this.duration,
        isWin = this.isWin
    )
}

// Маппинг из Domain в Entity (для сохранения в БД)
fun PlayerMatch.toEntity(accountId: Long): PlayerMatchEntity {
    return PlayerMatchEntity(
        matchId = this.matchId,
        heroId = this.heroId,
        kills = this.kills,
        deaths = this.deaths,
        assists = this.assists,
        duration = this.duration,
        isWin = this.isWin,
        accountId = accountId
    )
}