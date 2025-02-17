package com.example.bestpractices.dev.data.api

import com.example.bestpractices.dev.data.model.PlayerMatchResponse
import com.example.bestpractices.dev.data.model.PlayerStatsResponse
import retrofit2.http.GET
import retrofit2.http.Path

interface OpenDotaApiService {

    @GET("players/{account_id}")
    suspend fun getPlayerStats(@Path("account_id") accountId: Long): PlayerStatsResponse

    @GET("players/{account_id}/matches")
    suspend fun getPlayerMatches(@Path("account_id") accountId: Long): List<PlayerMatchResponse>
}