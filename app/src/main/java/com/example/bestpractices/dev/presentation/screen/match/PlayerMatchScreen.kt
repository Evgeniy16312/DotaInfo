package com.example.bestpractices.dev.presentation.screen.match

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.bestpractices.R
import com.example.bestpractices.dev.domain.model.PlayerMatch
import com.example.bestpractices.dev.presentation.viewmodel.PlayerMatchViewModel
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map


@Composable
fun PlayerMatchScreen(
    viewModel: PlayerMatchViewModel = hiltViewModel(),
    accountId: Long
) {
    val state by viewModel.state.collectAsState()

    LaunchedEffect(accountId) {
        viewModel.loadPlayerMatches(accountId)
    }

    when (val currentState = state) {
        is PlayerMatchState.Idle -> LoadingView()
        is PlayerMatchState.Loading -> LoadingView()
        is PlayerMatchState.Success -> MatchListView(viewModel, accountId)
        is PlayerMatchState.Error -> ErrorView(
            currentState.message,
            viewModel,
            accountId
        )
    }
}

@Composable
fun MatchListView(viewModel: PlayerMatchViewModel, accountId: Long) {
    val state by viewModel.state.collectAsState()
    val matches = (state as? PlayerMatchState.Success)?.matches.orEmpty()
    val isLoadingMore by viewModel.isLoadingMore.collectAsState()

    val listState = rememberLazyListState()

    // Отслеживание конца списка для подгрузки новых данных
    LaunchedEffect(listState) {
        snapshotFlow { listState.layoutInfo.visibleItemsInfo }
            .map { it.lastOrNull()?.index ?: 0 }
            .distinctUntilChanged()
            .collect { lastVisibleIndex ->
                if (lastVisibleIndex >= matches.size - 5 && !isLoadingMore) {
                    viewModel.loadMoreMatches(accountId)
                }
            }
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
            .padding(horizontal = 16.dp, vertical = 8.dp),
        state = listState,
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(matches) { match ->
            MatchItem(match)
        }

        // Показать индикатор загрузки при подгрузке новых данных
        if (isLoadingMore) {
            item {
                Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator()
                }
            }
        }
    }
}


@Composable
fun MatchItem(match: PlayerMatch) {
    val isWin = match.isWin
    val resultText = if (isWin) "Victory" else "Defeat"
    val resultIcon = if (isWin) R.drawable.win else R.drawable.lose

    val gradientColors = if (isWin) {
        listOf(Color(0xFF4CAF50), Color(0xFF087F23))
    } else {
        listOf(Color(0xFFD32F2F), Color(0xFF7B1FA2))
    }

    ElevatedCard(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.elevatedCardColors(
            containerColor = MaterialTheme.colorScheme.surface
        )
    ) {
        Column {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(brush = Brush.horizontalGradient(gradientColors))
                    .padding(vertical = 16.dp),
                contentAlignment = Alignment.Center
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    Icon(
                        painter = painterResource(id = resultIcon),
                        contentDescription = resultText,
                        modifier = Modifier.size(48.dp),
                        tint = Color.White
                    )

                    Spacer(modifier = Modifier.width(12.dp))

                    Text(
                        text = resultText.uppercase(),
                        color = Color.White,
                        fontWeight = FontWeight.Bold,
                        style = MaterialTheme.typography.titleLarge
                    )
                }
            }

            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "Match ID: ${match.matchId}",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                )

                Text(
                    text = "Hero ID: ${match.heroId}",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )

                Spacer(modifier = Modifier.height(8.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(text = "Kills: ${match.kills}", fontWeight = FontWeight.Medium)
                    Text(text = "Deaths: ${match.deaths}", fontWeight = FontWeight.Medium)
                    Text(text = "Assists: ${match.assists}", fontWeight = FontWeight.Medium)
                }

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = "Duration: ${match.duration / 60} min",
                    style = MaterialTheme.typography.bodySmall
                )
            }
        }
    }
}

@Composable
fun LoadingView() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator()
    }
}

@Composable
fun ErrorView(message: String, viewModel: PlayerMatchViewModel, accountId: Long) {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "Ошибка: $message",
            color = MaterialTheme.colorScheme.error,
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.bodyLarge
        )
        Spacer(modifier = Modifier.height(16.dp))
        Button(onClick = { viewModel.loadPlayerMatches(accountId) }) {
            Text("Попробовать снова")
        }
    }
}

@Preview(showBackground = true)
@Composable
fun MatchItemPreview() {
    MaterialTheme {
        MatchItem(
            match = PlayerMatch(
                matchId = 123456789,
                heroId = 1,
                kills = 10,
                deaths = 2,
                assists = 15,
                duration = 3200,
                isWin = true
            )
        )
    }
}