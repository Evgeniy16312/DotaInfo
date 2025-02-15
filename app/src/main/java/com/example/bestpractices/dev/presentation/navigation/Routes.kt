package com.example.bestpractices.dev.presentation.navigation

sealed class Screen(val route: String) {
    object NumberFact : Screen("Home")
    object SecondScreen : Screen("Favorite") {
        fun createRoute(id: String) = "second_screen/$id"
    }
}