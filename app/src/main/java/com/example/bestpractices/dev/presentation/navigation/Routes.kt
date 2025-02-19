package com.example.bestpractices.dev.presentation.navigation

sealed class Screen(val route: String) {
    object Account : Screen("Account")
    object PlayerMatch : Screen("Match")
    object PlayerStats : Screen("Stats")
}