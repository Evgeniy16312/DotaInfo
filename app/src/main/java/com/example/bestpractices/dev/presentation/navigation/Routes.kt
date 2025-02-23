package com.example.bestpractices.dev.presentation.navigation

sealed class Screen(val route: String) {
    object Account : Screen("account")
    object PlayerMatch : Screen("match")
    object PlayerStats : Screen("stats")
    object Heroes : Screen("heroes")
}