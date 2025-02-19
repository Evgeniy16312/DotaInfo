package com.example.bestpractices.dev.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.bestpractices.dev.domain.usecase.GetPlayerMatchUseCase
import com.example.bestpractices.dev.presentation.screen.match.PlayerMatchState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PlayerMatchViewModel @Inject constructor(
    private val getPlayerMatchUseCase: GetPlayerMatchUseCase
) : ViewModel() {

    private val _state = MutableStateFlow<PlayerMatchState>(PlayerMatchState.Idle)
    val state: StateFlow<PlayerMatchState> = _state.asStateFlow()

    fun loadPlayerMatches(accountId: Long) {
        viewModelScope.launch {
            _state.value = PlayerMatchState.Loading
            try {
                val matches = getPlayerMatchUseCase(accountId)
                _state.value = PlayerMatchState.Success(matches)
            } catch (e: Exception) {
                _state.value = PlayerMatchState.Error(e.localizedMessage ?: "Unknown error")
            }
        }
    }
}