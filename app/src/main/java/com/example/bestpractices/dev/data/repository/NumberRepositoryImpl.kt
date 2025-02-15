package com.example.bestpractices.dev.data.repository

import com.example.bestpractices.dev.data.api.NumberApiService
import com.example.bestpractices.dev.data.mapper.NumberFactMapper
import com.example.bestpractices.dev.domain.model.NumberFact
import com.example.bestpractices.dev.domain.repository.NumberRepository
import javax.inject.Inject

class NumberRepositoryImpl @Inject constructor(
    private val apiService: NumberApiService,
    private val mapper: NumberFactMapper
) : NumberRepository {
    override suspend fun getRandomFact(): NumberFact {
        val response = apiService.getRandomFact()
        return mapper.mapFromResponse(response)
    }
}