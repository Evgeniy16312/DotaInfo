package com.example.bestpractices.dev.presentation.screen.match

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.bestpractices.dev.domain.model.PlayerMatch
import com.example.bestpractices.dev.presentation.viewmodel.PlayerMatchViewModel

@Composable
fun PlayerMatchScreen(
    viewModel: PlayerMatchViewModel = hiltViewModel(),
    accountId: Long
) {
    val state by viewModel.state.collectAsState()

    LaunchedEffect(accountId) {
        viewModel.loadPlayerMatches(accountId)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
    ) {
        when (val currentState = state) {
            is PlayerMatchState.Idle -> Text("Загружаем данные...")
            is PlayerMatchState.Loading -> LoadingView()
            is PlayerMatchState.Success -> MatchListView(currentState.matches)
            is PlayerMatchState.Error -> ErrorView(
                currentState.message,
                viewModel,
                accountId
            )
        }
    }
}

@Composable
fun MatchListView(matches: List<PlayerMatch>) {
    LazyColumn {
        items(matches) { match ->
            MatchItem(match)
        }
    }
}

@Composable
fun MatchItem(match: PlayerMatch) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(text = "Match ID: ${match.matchId}", fontWeight = FontWeight.Bold)
            Text(text = "Hero ID: ${match.heroId}")
            Text(text = "Kills: ${match.kills}, Deaths: ${match.deaths}, Assists: ${match.assists}")
            Text(text = "Duration: ${match.duration / 60} min")
            Text(
                text = if (match.isWin) "Victory" else "Defeat",
                color = if (match.isWin) Color.Green else Color.Red
            )
        }
    }
}

@Composable
fun LoadingView() {
    CircularProgressIndicator()
}

@Composable
fun ErrorView(message: String, viewModel: PlayerMatchViewModel, accountId: Long) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text("Ошибка: $message", color = Color.Red, textAlign = TextAlign.Center)
        Spacer(modifier = Modifier.height(16.dp))
        Button(onClick = { viewModel.loadPlayerMatches(accountId) }) {
            Text("Попробовать снова")
        }
    }
}