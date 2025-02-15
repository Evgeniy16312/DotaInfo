package com.example.bestpractices.dev.presentation.navigation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.padding
import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.Scaffold
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.bestpractices.dev.presentation.screen.numberfactscreen.NumberFactScreen
import com.example.bestpractices.dev.presentation.screen.secondscreen.SecondScreen

@Composable
fun AppNavigation() {
    val navController = rememberNavController()
    val currentRoute = navController.currentBackStackEntryAsState().value?.destination?.route

    Scaffold(
        bottomBar = {
            BottomNavigationBar(navController, currentRoute)
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = Screen.NumberFact.route,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable(Screen.NumberFact.route) {
                NumberFactScreen()
            }
            composable(Screen.SecondScreen.route) {
                SecondScreen(id = "test")
            }
        }
    }
}

@Composable
fun BottomNavigationBar(navController: NavController, currentRoute: String?) {
    BottomNavigation {
        val items = listOf(
            Screen.NumberFact to Icons.Default.Home,
            Screen.SecondScreen to Icons.Default.Favorite
        )
        items.forEach { (screen, icon) ->
            BottomNavigationItem(
                modifier = Modifier
                    .background(MaterialTheme.colorScheme.primary),
                icon = { Icon(icon, contentDescription = null) },
                label = { Text(screen.route) },
                selected = currentRoute == screen.route,
                onClick = {
                    navController.navigate(screen.route) {
                        popUpTo(navController.graph.startDestinationId)
                        launchSingleTop = true
                    }
                }
            )
        }
    }
}