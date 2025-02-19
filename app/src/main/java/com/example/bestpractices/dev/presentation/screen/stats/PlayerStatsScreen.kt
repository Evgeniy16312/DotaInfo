package com.example.bestpractices.dev.presentation.screen.stats

import android.content.Context
import android.content.Intent
import android.net.Uri
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.FilledTonalButton
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
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
        is PlayerStatsState.Loading -> {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator(color = MaterialTheme.colorScheme.primary)
            }
        }

        is PlayerStatsState.Success -> {
            val player = currentState.playerStats

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                verticalArrangement = Arrangement.Top,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                ElevatedCard(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    elevation = CardDefaults.elevatedCardElevation(defaultElevation = 6.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp)
                    ) {
                        AsyncImage(
                            model = player.avatarUrl,
                            contentDescription = "Аватар",
                            modifier = Modifier
                                .size(120.dp)
                                .clip(CircleShape)
                        )
                        Spacer(modifier = Modifier.height(12.dp))
                        Text(
                            text = player.playerName,
                            style = MaterialTheme.typography.headlineSmall,
                            fontWeight = FontWeight.Bold
                        )
                        if (player.realName != "N/A") {
                            Text(
                                text = "Настоящее имя: ${player.realName}",
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
                        if (player.lastLogin != "Неизвестно") {
                            Text(
                                text = "Последний вход: ${player.lastLogin}",
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                        if (player.country != "N/A") {
                            Text(
                                text = "Страна: ${player.country}",
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(20.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    StatCard(
                        title = "MMR Solo",
                        value = player.soloCompetitiveRank?.toString() ?: "-"
                    )
                    StatCard(title = "MMR Party", value = player.competitiveRank?.toString() ?: "-")
                    StatCard(title = "Лидерборд", value = player.leaderboardRank?.toString() ?: "-")
                }

                Spacer(modifier = Modifier.height(20.dp))

                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                        StatCard("Cheese", player.cheese.toString())
                        StatCard("Dota Plus", if (player.isDotaPlusSubscriber) "Да" else "Нет")
                    }
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                        StatCard("Контрибьютор", if (player.isContributor) "Да" else "Нет")
                        StatCard("Подписчик", if (player.isSubscriber) "Да" else "Нет")
                    }
                }


                Spacer(modifier = Modifier.height(20.dp))

                FilledTonalButton(
                    onClick = { openProfile(context, player.profileUrl) },
                    colors = ButtonDefaults.filledTonalButtonColors()
                ) {
                    Text(text = "Открыть в Steam")
                }
            }
        }

        is PlayerStatsState.Error -> {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text(
                    text = currentState.message,
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }

        PlayerStatsState.Idle -> {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text(text = "Введите данные", style = MaterialTheme.typography.bodyMedium)
            }
        }
    }
}

@Composable
fun StatCard(title: String, value: String) {
    OutlinedCard(
        modifier = Modifier
            .size(width = 100.dp, height = 80.dp),
        shape = RoundedCornerShape(16.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.primary
            )
            Text(
                text = value,
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

fun openProfile(context: Context, url: String?) {
    url?.let {
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(it))
        context.startActivity(intent)
    }
}

@Preview(showBackground = true, device = "id:pixel_6", showSystemUi = true)
@Composable
fun PlayerStatsScreenPreview() {
    val samplePlayerStats = PlayerStats(
        accountId = 321580662,
        playerName = "Стекло",
        realName = "Raddan",
        avatarUrl = "https://steamcdn-a.akamaihd.net/steamcommunity/public/images/avatars/xx/xx_full.jpg",
        avatarMediumUrl = "https://steamcdn-a.akamaihd.net/steamcommunity/public/images/avatars/xx/xx_medium.jpg",
        soloCompetitiveRank = null,
        competitiveRank = null,
        leaderboardRank = 27,
        lastLogin = "2023-01-26T10:04:18.483Z",
        country = "CH",
        profileUrl = "https://steamcommunity.com/profiles/76561198006409530",
        steamId = "76561198006409530",
        isDotaPlusSubscriber = true,
        cheese = 10,
        isContributor = false,
        isSubscriber = false
    )

    MaterialTheme {
        PlayerStatsContent(player = samplePlayerStats, onProfileClick = {})
    }
}

@Composable
fun PlayerStatsContent(player: PlayerStats, onProfileClick: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        ElevatedCard(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            elevation = CardDefaults.elevatedCardElevation(defaultElevation = 6.dp)
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                AsyncImage(
                    model = player.avatarUrl,
                    contentDescription = "Аватар",
                    modifier = Modifier
                        .size(120.dp)
                        .clip(CircleShape)
                )
                Spacer(modifier = Modifier.height(12.dp))
                Text(
                    text = player.playerName,
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold
                )
                player.realName?.let {
                    Text(
                        text = "Настоящее имя: $it",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
                Text(
                    text = "ID: ${player.accountId}",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                player.lastLogin?.let {
                    Text(
                        text = "Последний вход: $it",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
                player.country?.let {
                    Text(
                        text = "Страна: $it",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            StatCard(title = "MMR Solo", value = player.soloCompetitiveRank?.toString() ?: "-")
            StatCard(title = "MMR Party", value = player.competitiveRank?.toString() ?: "-")
            StatCard(title = "Лидерборд", value = player.leaderboardRank?.toString() ?: "-")
        }

        Spacer(modifier = Modifier.height(20.dp))

        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                StatCard("Cheese", player.cheese.toString())
                StatCard("Dota Plus", if (player.isDotaPlusSubscriber) "Да" else "Нет")
            }
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                StatCard("Контрибьютор", if (player.isContributor) "Да" else "Нет")
                StatCard("Подписчик", if (player.isSubscriber) "Да" else "Нет")
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        FilledTonalButton(
            onClick = onProfileClick,
            colors = ButtonDefaults.filledTonalButtonColors()
        ) {
            Text(text = "Открыть в Steam")
        }
    }
}
