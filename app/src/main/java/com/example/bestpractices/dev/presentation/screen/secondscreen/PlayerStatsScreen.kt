package com.example.bestpractices.dev.presentation.screen.secondscreen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.example.bestpractices.dev.presentation.viewmodel.PlayerStatsViewModel

@Composable
fun PlayerStatsScreen(viewModel: PlayerStatsViewModel = hiltViewModel()) {

    val state by viewModel.state.collectAsState()
    var accountId by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        OutlinedTextField(
            value = accountId,
            onValueChange = { accountId = it },
            label = { Text("Введите account_id") },
            keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number),
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {
                if (accountId.isNotEmpty()) {
                    viewModel.fetchPlayerStats(accountId.toLong())
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Получить статистику")
        }

        Spacer(modifier = Modifier.height(16.dp))

        when (val currentState = state) {
            is PlayerStatsState.Idle -> {
            }

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
        }
    }
}