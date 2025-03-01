package com.example.bestpractices.dev.domain.repository

import com.example.bestpractices.dev.domain.model.Heroes

interface HeroesRepository {
    suspend fun getHeroes(): List<Heroes>
    suspend fun updateHeroes(): List<Heroes>
}