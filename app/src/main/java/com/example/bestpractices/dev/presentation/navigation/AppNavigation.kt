package com.example.bestpractices.dev.presentation.navigation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.padding
import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.Scaffold
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.bestpractices.dev.presentation.screen.account.AccountScreen
import com.example.bestpractices.dev.presentation.screen.match.PlayerMatchScreen
import com.example.bestpractices.dev.presentation.screen.stats.PlayerStatsScreen

@Composable
fun AppNavigation() {
    val navController = rememberNavController()
    val currentRoute = navController.currentBackStackEntryAsState().value?.destination?.route

    Scaffold(
        bottomBar = {
            BottomNavigationBar(navController = navController, currentRoute = currentRoute)
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = Screen.Account.route,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable(Screen.Account.route) {
                AccountScreen(navController = navController)
            }
            composable(
                route = Screen.PlayerMatch.route + "?accountId={accountId}",
                arguments = listOf(navArgument("accountId") { type = NavType.LongType })
            ) { backStackEntry ->
                val accountId = backStackEntry.arguments?.getLong("accountId") ?: run {
                    navController.popBackStack()
                    return@composable
                }
                PlayerMatchScreen(accountId = accountId)
            }
            composable(
                route = Screen.PlayerStats.route + "?accountId={accountId}",
                arguments = listOf(navArgument("accountId") { type = NavType.LongType })
            ) { backStackEntry ->
                val accountId = backStackEntry.arguments?.getLong("accountId") ?: run {
                    navController.popBackStack()
                    return@composable
                }
                PlayerStatsScreen(accountId = accountId)
            }
        }
    }
}

@Composable
fun BottomNavigationBar(navController: NavController, currentRoute: String?) {
    BottomNavigation {
        val items = listOf(
            Screen.PlayerMatch to Icons.Default.PlayArrow,
            Screen.PlayerStats to Icons.Default.Person
        )

        items.forEach { (screen, icon) ->
            BottomNavigationItem(
                modifier = Modifier.background(MaterialTheme.colorScheme.primary),
                icon = { Icon(icon, contentDescription = null) },
                label = { Text(screen.route) },
                selected = currentRoute?.startsWith(screen.route) == true,
                onClick = {
                    val accountId = navController.currentBackStackEntry
                        ?.arguments?.getLong("accountId")

                    val destination = if (accountId != null) {
                        "${screen.route}?accountId=$accountId"
                    } else {
                        screen.route
                    }

                    navController.navigate(destination) {
                        popUpTo(navController.graph.startDestinationId) { inclusive = false }
                        launchSingleTop = true
                    }
                }
            )
        }
    }
}