package com.example.bestpractices.dev.domain.usecase

import com.example.bestpractices.dev.domain.model.NumberFact
import com.example.bestpractices.dev.domain.repository.NumberRepository
import javax.inject.Inject

class GetRandomFact @Inject constructor(
    private val repository: NumberRepository
) {
    suspend operator fun invoke(): NumberFact {
        return repository.getRandomFact()
    }
}