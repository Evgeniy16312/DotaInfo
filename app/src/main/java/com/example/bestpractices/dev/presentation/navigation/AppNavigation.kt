package com.example.bestpractices.dev.presentation.navigation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.Scaffold
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.bestpractices.R
import com.example.bestpractices.dev.presentation.screen.account.AccountScreen
import com.example.bestpractices.dev.presentation.screen.heroes.HeroesScreen
import com.example.bestpractices.dev.presentation.screen.match.PlayerMatchScreen
import com.example.bestpractices.dev.presentation.screen.stats.PlayerStatsScreen

@Composable
fun AppNavigation() {
    val navController = rememberNavController()
    val currentBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = currentBackStackEntry?.destination?.route

    Scaffold(
        bottomBar = {
            if (currentRoute != Screen.Account.route) {
                BottomNavigationBar(navController = navController, currentRoute = currentRoute)
            }
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
            composable(Screen.Heroes.route) {
                HeroesScreen(navController = navController)
            }
        }
    }
}

@Composable
fun BottomNavigationBar(navController: NavController, currentRoute: String?) {
    BottomNavigation(
        modifier = Modifier
            .fillMaxWidth()
            .height(56.dp),
        backgroundColor = Color.Black
    ) {
        val items = listOf(
            Screen.PlayerStats to R.drawable.account,
            Screen.PlayerMatch to R.drawable.match,
            Screen.Heroes to R.drawable.heroes
        )

        items.forEach { (screen, iconRes) ->
            val isSelected = currentRoute?.startsWith(screen.route) == true
            BottomNavigationItem(
                icon = {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            painter = painterResource(id = iconRes),
                            contentDescription = null,
                            modifier = Modifier.size(32.dp),
                            tint = if (isSelected) Color.White else Color.LightGray
                        )
                    }
                },
                label = { Text("") },
                selected = isSelected,
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
                },
                selectedContentColor = Color.White,
                unselectedContentColor = Color.Gray
            )
        }
    }
}