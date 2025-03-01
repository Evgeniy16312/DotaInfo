package com.example.bestpractices.dev.domain.usecase

import com.example.bestpractices.dev.domain.model.Heroes
import com.example.bestpractices.dev.domain.repository.HeroesRepository
import javax.inject.Inject

class GetHeroesUseCase @Inject constructor(
    private val repository: HeroesRepository
) {
    suspend operator fun invoke(): List<Heroes> {
        return repository.getHeroes()
    }

    suspend fun updateHeroes(): List<Heroes> {
        return repository.updateHeroes()
    }
}