package com.example.bestpractices.dev.presentation.screen.match

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
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
            is PlayerMatchState.Success -> MatchListView(viewModel, accountId)
            is PlayerMatchState.Error -> ErrorView(
                currentState.message,
                viewModel,
                accountId
            )
        }
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun MatchListView(viewModel: PlayerMatchViewModel, accountId: Long) {
    val state by viewModel.state.collectAsState()
    val matches = (state as? PlayerMatchState.Success)?.matches.orEmpty()
    val refreshState = rememberPullRefreshState(
        refreshing = state is PlayerMatchState.Loading,
        onRefresh = { viewModel.loadPlayerMatches(accountId, refresh = true) }
    )

    val listState = rememberLazyListState()

    // Отслеживаем конец списка для подгрузки новых данных
    LaunchedEffect(listState) {
        snapshotFlow { listState.layoutInfo.visibleItemsInfo }
            .map { it.lastOrNull()?.index ?: 0 }
            .distinctUntilChanged()
            .collect { lastVisibleIndex ->
                if (lastVisibleIndex >= matches.size - 5) { // Подгружать за 5 элементов до конца
                    viewModel.loadMoreMatches(accountId)
                }
            }
    }

    Box(modifier = Modifier.fillMaxSize().pullRefresh(refreshState)) {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            state = listState,
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(matches) { match ->
                MatchItem(match)
            }

            // Показать индикатор загрузки при подгрузке новых данных
            item {
                if (viewModel.isLoadingMore()) {
                    Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                        CircularProgressIndicator()
                    }
                }
            }
        }

        PullRefreshIndicator(
            refreshing = state is PlayerMatchState.Loading,
            state = refreshState,
            modifier = Modifier.align(Alignment.TopCenter)
        )
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