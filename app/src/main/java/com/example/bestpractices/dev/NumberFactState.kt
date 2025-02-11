package com.example.bestpractices.dev

sealed interface NumberFactState {
    object Idle : NumberFactState
    object Loading : NumberFactState
    data class Success(val factResponse: NumberFactResponse) : NumberFactState
    data class Error(val message: String) : NumberFactState
}

sealed interface NumberFactIntent {
    object FetchRandomFact : NumberFactIntent
}