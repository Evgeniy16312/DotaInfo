package com.example.bestpractices.dev.data.api

import com.example.bestpractices.dev.data.model.HeroesResponse
import com.example.bestpractices.dev.data.model.PlayerMatchResponse
import com.example.bestpractices.dev.data.model.PlayerStatsResponse
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface OpenDotaApiService {

    @GET("heroes")
    suspend fun getHeroes(): List<HeroesResponse>

    @GET("players/{account_id}")
    suspend fun getPlayerStats(@Path("account_id") accountId: Long): PlayerStatsResponse

    @GET("players/{account_id}/matches")
    suspend fun getPlayerMatches(
        @Path("account_id") accountId: Long,
        @Query("limit") limit: Int,
        @Query("offset") offset: Int
    ): List<PlayerMatchResponse>

}