package com.example.bestpractices.dev.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.bestpractices.dev.presentation.screen.numberfactscreen.NumberFactIntent
import com.example.bestpractices.dev.presentation.screen.numberfactscreen.NumberFactState
import com.example.bestpractices.dev.domain.usecase.GetRandomFact
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val getRandomFact: GetRandomFact
) : ViewModel() {
    private val _state = MutableStateFlow<NumberFactState>(NumberFactState.Idle)
    val state: StateFlow<NumberFactState> = _state

    fun processIntent(intent: NumberFactIntent) {
        when (intent) {
            is NumberFactIntent.FetchRandomFact -> fetchRandomFact()
        }
    }

    private fun fetchRandomFact() {
        viewModelScope.launch {
            _state.value = NumberFactState.Loading
            try {
                val factResponse = getRandomFact()
                _state.value = NumberFactState.Success(factResponse)
            } catch (e: Exception) {
                _state.value = NumberFactState.Error(e.message ?: "Unknown error")
            }
        }
    }
}