package com.example.bestpractices.dev

import com.google.gson.Gson
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

import retrofit2.http.GET

class NumberRepository(private val apiService: NumberApiService) {
    suspend fun getRandomFact(): NumberFactResponse {
        return apiService.getRandomFact()
    }
}

interface NumberApiService {
    @GET("/random/trivia?json")
    suspend fun getRandomFact(): NumberFactResponse
}

object RetrofitInstance {
    private const val BASE_URL = "http://numbersapi.com/"

    val api: NumberApiService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create(Gson()))
            .build()
            .create(NumberApiService::class.java)
    }
}