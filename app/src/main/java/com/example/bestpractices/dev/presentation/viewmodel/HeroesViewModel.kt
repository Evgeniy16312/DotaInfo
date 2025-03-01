package com.example.bestpractices.dev.presentation.viewmodel

import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.bestpractices.dev.domain.usecase.GetHeroesUseCase
import com.example.bestpractices.dev.presentation.screen.heroes.HeroesState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HeroesViewModel @Inject constructor(
    private val getHeroesUseCase: GetHeroesUseCase
) : ViewModel() {

    private val _heroesState = mutableStateOf<HeroesState>(HeroesState.Idle)
    val heroesState: MutableState<HeroesState> = _heroesState

    fun loadHeroes() {
        viewModelScope.launch {
            _heroesState.value = HeroesState.Loading
            try {
                val heroes = getHeroesUseCase()
                _heroesState.value = HeroesState.Success(heroes)
                updateHeroesIfNeeded()
            } catch (e: Exception) {
                _heroesState.value = HeroesState.Error(e.message ?: "Unknown error")
            }
        }
    }

    private fun updateHeroesIfNeeded() {
        viewModelScope.launch {
            try {
                val updatedHeroes = getHeroesUseCase.updateHeroes()
                _heroesState.value = HeroesState.Success(updatedHeroes)
            } catch (e: Exception) {
                Log.e("updateHeroesIfNeeded", e.message.orEmpty())
            }
        }
    }
}