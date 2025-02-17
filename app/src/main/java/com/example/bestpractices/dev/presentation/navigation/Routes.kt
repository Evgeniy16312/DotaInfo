package com.example.bestpractices.dev.presentation.navigation

sealed class Screen(val route: String) {
    object NumberFact : Screen("Home")
    object PlayerStats : Screen("Favorite")
}