package com.example.bestpractices.dev.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.bestpractices.dev.domain.model.PlayerMatch
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

    private var currentPage = 0
    private var isLastPage = false
    private var isLoadingMore = false

    fun loadPlayerMatches(accountId: Long, refresh: Boolean = false) {
        viewModelScope.launch {
            if (refresh) {
                currentPage = 0
                isLastPage = false
                _state.value = PlayerMatchState.Loading
            }

            try {
                val matches = getPlayerMatchUseCase(accountId, page = 0, pageSize = 20)

                val currentMatches = (state.value as? PlayerMatchState.Success)?.matches.orEmpty()

                if (refresh && matches == currentMatches) {
                    return@launch // Выходим из функции, если список не изменился
                }

                _state.value = PlayerMatchState.Success(matches)
                currentPage = 1 // Сбрасываем страницу после успешного обновления

            } catch (e: Exception) {
                _state.value = PlayerMatchState.Error(e.localizedMessage ?: "Unknown error")
            }
        }
    }

    fun loadMoreMatches(accountId: Long) {
        if (isLoadingMore || isLastPage) return

        isLoadingMore = true
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
                // Ошибку игнорируем, не сбрасываем основной список
            }
            isLoadingMore = false
        }
    }

    fun isLoadingMore(): Boolean = isLoadingMore
}