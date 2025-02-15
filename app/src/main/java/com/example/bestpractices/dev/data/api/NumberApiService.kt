package com.example.bestpractices.dev.data.api

import com.example.bestpractices.dev.data.model.NumberFactResponse
import retrofit2.http.GET

interface NumberApiService {
    @GET("/random/trivia?json")
    suspend fun getRandomFact(): NumberFactResponse
}