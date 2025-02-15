package com.example.bestpractices.dev.domain.repository

import com.example.bestpractices.dev.domain.model.NumberFact

interface NumberRepository {
    suspend fun getRandomFact(): NumberFact
}