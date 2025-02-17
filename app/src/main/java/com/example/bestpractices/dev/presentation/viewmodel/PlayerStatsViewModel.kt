package com.example.bestpractices.dev.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.bestpractices.dev.domain.usecase.GetPlayerStatsUseCase
import com.example.bestpractices.dev.presentation.screen.secondscreen.PlayerStatsState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PlayerStatsViewModel @Inject constructor(
    private val getPlayerStatsUseCase: GetPlayerStatsUseCase
) : ViewModel() {

    private val _state = MutableStateFlow<PlayerStatsState>(PlayerStatsState.Idle)
    val state: StateFlow<PlayerStatsState> = _state

    fun fetchPlayerStats(accountId: Long) {
        viewModelScope.launch {
            _state.value = PlayerStatsState.Loading
            try {
                val stats = getPlayerStatsUseCase(accountId)
                _state.value = PlayerStatsState.Success(stats)
            } catch (e: Exception) {
                _state.value = PlayerStatsState.Error(e.message ?: "Unknown error")
            }
        }
    }
}