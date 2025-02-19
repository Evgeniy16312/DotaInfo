package com.example.bestpractices.dev.presentation.screen.stats

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.example.bestpractices.dev.presentation.viewmodel.PlayerStatsViewModel

@Composable
fun PlayerStatsScreen(
    viewModel: PlayerStatsViewModel = hiltViewModel(),
    accountId: Long
) {
    val state by viewModel.state.collectAsState()

    LaunchedEffect(accountId) {
        viewModel.fetchPlayerStats(accountId)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        when (val currentState = state) {
            is PlayerStatsState.Loading -> {
                CircularProgressIndicator()
            }

            is PlayerStatsState.Success -> {
                Column {
                    Text(text = "ID аккаунта: ${currentState.playerStats.accountId}")
                    Text(text = "Имя игрока: ${currentState.playerStats.playerName}")
                    Text(text = "Ранг: ${currentState.playerStats.rankTier}")
                    AsyncImage(
                        model = currentState.playerStats.avatarUrl,
                        contentDescription = "Аватар игрока",
                        modifier = Modifier.size(100.dp)
                    )
                }
            }

            is PlayerStatsState.Error -> {
                Text(text = currentState.message, color = Color.Red)
            }

            PlayerStatsState.Idle ->  Text(text = "Введите данные")
        }
    }
}