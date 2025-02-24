package com.example.bestpractices.dev.presentation.screen.stats

import android.content.Context
import android.content.Intent
import android.net.Uri
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
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.example.bestpractices.R
import com.example.bestpractices.dev.domain.model.PlayerStats
import com.example.bestpractices.dev.presentation.viewmodel.PlayerStatsViewModel

@Composable
fun PlayerStatsScreen(
    viewModel: PlayerStatsViewModel = hiltViewModel(),
    accountId: Long
) {
    val state by viewModel.state.collectAsState()
    val context = LocalContext.current

    LaunchedEffect(accountId) {
        viewModel.fetchPlayerStats(accountId)
    }

    when (val currentState = state) {
        is PlayerStatsState.Loading -> LoadingView()
        is PlayerStatsState.Success -> PlayerStatsContent(currentState.playerStats, context)
        is PlayerStatsState.Error -> ErrorView(currentState.message)
        PlayerStatsState.Idle -> IdleView()
    }
}

@Composable
fun PlayerStatsContent(player: PlayerStats, context: Context) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(20.dp)
    ) {
        PlayerCard(player = player)

        StatsGrid(
            listOf(
                Triple("MMR Solo", player.soloCompetitiveRank?.toString() ?: "-", R.drawable.solo),
                Triple("MMR Party", player.competitiveRank?.toString() ?: "-", R.drawable.party),
                Triple(
                    "Лидерборд",
                    player.leaderboardRank?.toString() ?: "-",
                    R.drawable.leader_board
                )
            )
        )

        StatsGrid(
            listOf(
                Triple("Cheese", player.cheese.toString(), R.drawable.cheese),
                Triple(
                    "Dota Plus",
                    if (player.isDotaPlusSubscriber) "Да" else "Нет",
                    R.drawable.dota_plus
                ),
                Triple(
                    "Контрибьютор",
                    if (player.isContributor) "Да" else "Нет",
                    R.drawable.countributer
                ),
                Triple("Подписчик", if (player.isSubscriber) "Да" else "Нет", R.drawable.subscriber)
            )
        )

        FilledTonalButton(
            onClick = { openProfile(context, player.profileUrl) },
            colors = ButtonDefaults.filledTonalButtonColors(),
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp)
        ) {
            Text(text = "Открыть в Steam")
        }
    }
}

@Composable
fun PlayerCard(player: PlayerStats) {
    ElevatedCard(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        shape = MaterialTheme.shapes.medium,
        colors = CardDefaults.elevatedCardColors(
            containerColor = MaterialTheme.colorScheme.surface
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            AsyncImage(
                model = player.avatarUrl,
                contentDescription = "Аватар",
                modifier = Modifier
                    .size(100.dp)
                    .clip(CircleShape)
            )

            Spacer(modifier = Modifier.width(16.dp))

            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = player.playerName,
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                )

                player.realName.takeIf { it != "N/A" }?.let {
                    Text(
                        text = "Имя: $it",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }

                Text(
                    text = "ID: ${player.accountId}",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )

                Spacer(modifier = Modifier.height(8.dp))

                player.lastLogin.takeIf { it != "Неизвестно" }?.let {
                    Text(
                        text = "Последний вход: $it",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }

                player.country.takeIf { it != "N/A" }?.let {
                    Text(
                        text = "Страна: $it",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }
    }
}

@Composable
fun StatsGrid(items: List<Triple<String, String, Int>>) {
    LazyVerticalGrid(
        columns = GridCells.Adaptive(minSize = 120.dp),
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(12.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        items(items) { (title, value, icon) ->
            StatCard(title, value, icon)
        }
    }
}

@Composable
fun StatCard(title: String, value: String, iconRes: Int) {
    OutlinedCard(
        modifier = Modifier
            .size(width = 120.dp, height = 100.dp),
        shape = MaterialTheme.shapes.medium
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(
                painter = painterResource(id = iconRes),
                contentDescription = title,
                modifier = Modifier.size(32.dp),
                tint = MaterialTheme.colorScheme.primary
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = title,
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Text(
                text = value,
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

// Заглушки состояний
@Composable
fun LoadingView() {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        CircularProgressIndicator()
    }
}

@Composable
fun ErrorView(message: String) {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Text(
            text = message,
            color = MaterialTheme.colorScheme.error,
            style = MaterialTheme.typography.bodyMedium
        )
    }
}

@Composable
fun IdleView() {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Text(text = "Введите данные", style = MaterialTheme.typography.bodyMedium)
    }
}

fun openProfile(context: Context, url: String?) {
    url?.let {
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(it))
        context.startActivity(intent)
    }
}

@Preview(showBackground = true)
@Composable
fun StatCardPreview() {
    MaterialTheme {
        StatCard("MMR Solo", "4000", R.drawable.leader_board)
    }
}