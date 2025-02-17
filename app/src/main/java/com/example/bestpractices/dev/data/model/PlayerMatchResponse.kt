package com.example.bestpractices.dev.data.model

import com.google.gson.annotations.SerializedName

data class PlayerMatchResponse(
    @SerializedName("match_id") val matchId: Long,
    @SerializedName("player_slot") val playerSlot: Int,
    @SerializedName("radiant_win") val radiantWin: Boolean,
    @SerializedName("duration") val duration: Int,
    @SerializedName("game_mode") val gameMode: Int,
    @SerializedName("hero_id") val heroId: Int,
    @SerializedName("kills") val kills: Int,
    @SerializedName("deaths") val deaths: Int,
    @SerializedName("assists") val assists: Int
)