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

    private val _isLoadingMore = MutableStateFlow(false)
    val isLoadingMore: StateFlow<Boolean> = _isLoadingMore.asStateFlow()

    private var currentPage = 0
    private var isLastPage = false

    fun loadPlayerMatches(accountId: Long, refresh: Boolean = false) {
        viewModelScope.launch {
            if (refresh) {
                currentPage = 0
                isLastPage = false
                _state.value = PlayerMatchState.Loading
            }

            try {
                val matches = getPlayerMatchUseCase(accountId, page = currentPage, pageSize = 20)
                val currentMatches =
                    if (refresh) emptyList() else (state.value as? PlayerMatchState.Success)?.matches.orEmpty()

                _state.value = PlayerMatchState.Success(currentMatches + matches)
                currentPage++

                if (matches.size < 20) {
                    isLastPage = true
                }
            } catch (e: Exception) {
                _state.value = PlayerMatchState.Error(e.localizedMessage ?: "Unknown error")
            }
        }
    }

    fun loadMoreMatches(accountId: Long) {
        if (_isLoadingMore.value || isLastPage) return

        _isLoadingMore.value = true
        viewModelScope.launch {
            try {
                val matches = getPlayerMatchUseCase(accountId, page = currentPage, pageSize = 20)
                val currentMatches = (state.value as? PlayerMatchState.Success)?.matches.orEmpty()
                _state.value = PlayerMatchState.Success(currentMatches + matches)

                if (matches.size < 20) {
                    isLastPage = true
                } else {
                    currentPage++
                }
            } catch (e: Exception) {
                _state.value = PlayerMatchState.Error(e.localizedMessage ?: "Unknown error")
            } finally {
                _isLoadingMore.value = false
            }
        }
    }
}