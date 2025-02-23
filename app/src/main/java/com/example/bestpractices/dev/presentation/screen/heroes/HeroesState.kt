package com.example.bestpractices.dev.presentation.screen.heroes

import com.example.bestpractices.dev.domain.model.Heroes

sealed class HeroesState {
    object Idle : HeroesState()
    object Loading : HeroesState()
    data class Success(val heroes: List<Heroes>) : HeroesState()
    data class Error(val message: String) : HeroesState()
}