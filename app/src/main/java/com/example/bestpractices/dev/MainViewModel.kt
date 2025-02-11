package com.example.bestpractices.dev

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class MainViewModel(
    private val repository: NumberRepository
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
                val factResponse = repository.getRandomFact()
                _state.value = NumberFactState.Success(factResponse)
            } catch (e: Exception) {
                _state.value = NumberFactState.Error(e.message ?: "Unknown error")
            }
        }
    }
}