package com.example.bestpractices.dev.data.model

import com.google.gson.annotations.SerializedName

data class PlayerStatsResponse(
    @SerializedName("solo_competitive_rank") val soloCompetitiveRank: Int?,
    @SerializedName("competitive_rank") val competitiveRank: Int?,
    @SerializedName("rank_tier") val rankTier: Int?,
    @SerializedName("leaderboard_rank") val leaderboardRank: Int?,
    @SerializedName("profile") val profile: PlayerProfile?
)

data class PlayerProfile(
    @SerializedName("account_id") val accountId: Long?,
    @SerializedName("personaname") val personaname: String?,
    @SerializedName("name") val name: String?,
    @SerializedName("plus") val plus: Boolean?,
    @SerializedName("cheese") val cheese: Int?,
    @SerializedName("steamid") val steamId: String?,
    @SerializedName("avatar") val avatar: String?,
    @SerializedName("avatarmedium") val avatarMedium: String?,
    @SerializedName("avatarfull") val avatarFull: String?,
    @SerializedName("profileurl") val profileUrl: String?,
    @SerializedName("last_login") val lastLogin: String?,
    @SerializedName("loccountrycode") val locCountryCode: String?,
    @SerializedName("is_contributor") val isContributor: Boolean?,
    @SerializedName("is_subscriber") val isSubscriber: Boolean?
)