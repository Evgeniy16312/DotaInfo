package com.example.bestpractices.dev.presentation.screen.numberfactscreen

import com.example.bestpractices.dev.domain.model.NumberFact

sealed interface NumberFactState {
    data object Idle : NumberFactState
    data object Loading : NumberFactState
    data class Success(val factResponse: NumberFact) : NumberFactState
    data class Error(val message: String) : NumberFactState
}

sealed interface NumberFactIntent {
    data object FetchRandomFact : NumberFactIntent
}