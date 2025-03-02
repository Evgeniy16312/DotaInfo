package com.example.bestpractices.dev.presentation.screen.stats

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.bestpractices.R
import com.example.bestpractices.dev.domain.model.PlayerStats
import com.example.bestpractices.dev.presentation.navigation.Screen
import com.example.bestpractices.dev.presentation.screen.PreferencesManager
import com.example.bestpractices.dev.presentation.viewmodel.PlayerStatsViewModel

@Composable
fun PlayerStatsScreen(
    navController: NavController,
    accountId: Long,
    preferencesManager: PreferencesManager,
    viewModel: PlayerStatsViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsState()
    val context = LocalContext.current

    LaunchedEffect(accountId) {
        viewModel.fetchPlayerStats(accountId)
    }

    when (val currentState = state) {
        is PlayerStatsState.Loading -> LoadingView()
        is PlayerStatsState.Success -> PlayerStatsContent(
            player = currentState.playerStats,
            context = context,
            navController = navController,
            preferencesManager = preferencesManager
        )

        is PlayerStatsState.Error -> ErrorView(message = currentState.message)
    }
}

@Composable
fun PlayerStatsContent(
    player: PlayerStats,
    context: Context,
    navController: NavController,
    preferencesManager: PreferencesManager
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
            .padding(top = 16.dp)
    ) {

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 16.dp)
                .padding(bottom = 16.dp)
        ) {

            ProfileHeader(player = player)

            Spacer(modifier = Modifier.height(16.dp))

            StatsSection(player = player)

        }

        ActionButtons(
            context = context,
            profileUrl = player.profileUrl,
            navController = navController,
            preferencesManager = preferencesManager,
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(horizontal = 16.dp)
                .padding(bottom = 16.dp)
        )
    }
}

@Composable
fun ProfileHeader(player: PlayerStats) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight(),
        shape = RoundedCornerShape(16.dp),
        color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.1f)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    brush = Brush.verticalGradient(
                        colors = listOf(Color(0xFF1a1a1a), Color(0xFFA30900).copy(alpha = 0.3f))
                    )
                )
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                AsyncImage(
                    model = player.avatarUrl,
                    contentDescription = "Аватар",
                    modifier = Modifier
                        .size(100.dp)
                        .clip(CircleShape)
                        .border(2.dp, Color.White.copy(alpha = 0.8f), CircleShape)
                )

                Spacer(modifier = Modifier.width(16.dp))

                Column(
                    modifier = Modifier.weight(1f)
                ) {
                    Text(
                        text = player.playerName,
                        style = MaterialTheme.typography.headlineLarge,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )

                    player.realName.takeIf { it != "N/A" }?.let {
                        Text(
                            text = it,
                            style = MaterialTheme.typography.bodyLarge,
                            color = Color.LightGray
                        )
                    }

                    Text(
                        text = "ID: ${player.accountId}",
                        style = MaterialTheme.typography.bodyMedium,
                        color = Color.Gray
                    )
                }
            }
        }
    }
}

@Composable
fun StatsSection(player: PlayerStats) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight(),
        shape = RoundedCornerShape(16.dp),
        color = MaterialTheme.colorScheme.surface.copy(alpha = 0.2f)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    brush = Brush.verticalGradient(
                        colors = listOf(Color(0xFF1a1a1a), Color(0xFFA30900).copy(alpha = 0.3f))
                    )
                )
        ) {
            LazyRow(
                modifier = Modifier
                    .padding(20.dp),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                items(
                    listOf(
                        Triple(
                            "Solo MMR",
                            player.soloCompetitiveRank?.toString() ?: "-",
                            R.drawable.solo
                        ),
                        Triple(
                            "Party MMR",
                            player.competitiveRank?.toString() ?: "-",
                            R.drawable.party
                        ),
                        Triple(
                            "Лидерборд",
                            player.leaderboardRank?.toString() ?: "-",
                            R.drawable.leader_board
                        ),
                        Triple(
                            "Dota Plus",
                            if (player.isDotaPlusSubscriber) "Да" else "Нет",
                            R.drawable.dota_plus
                        ),
                        Triple(
                            "Подписчик",
                            if (player.isSubscriber) "Да" else "Нет",
                            R.drawable.subscriber
                        ),
                        Triple(
                            "Контрибьютор",
                            if (player.isContributor) "Да" else "Нет",
                            R.drawable.countributer
                        ),
                        Triple("Cheese", player.cheese.toString(), R.drawable.cheese)
                    )
                ) { (title, value, icon) ->
                    StatItem(
                        title = title,
                        value = value,
                        icon = icon,
                        modifier = Modifier.width(110.dp)
                    )
                }
            }
        }
    }
}

@Composable
fun StatItem(title: String, value: String, icon: Int, modifier: Modifier = Modifier) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(
            painter = painterResource(id = icon),
            contentDescription = title,
            modifier = Modifier.size(32.dp),
            tint = Color.White
        )
        Spacer(modifier = Modifier.height(6.dp))
        Text(
            text = value,
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold,
            color = Color.White
        )
        Text(
            text = title,
            style = MaterialTheme.typography.bodyMedium,
            color = Color.Gray,
            textAlign = TextAlign.Center
        )
    }
}

@Composable
fun ActionButtons(
    context: Context,
    profileUrl: String?,
    navController: NavController,
    preferencesManager: PreferencesManager,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Button(
            onClick = { openProfile(context, profileUrl) },
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp),
            shape = RoundedCornerShape(12.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFA30900))
        ) {
            Text(
                text = "Открыть в Steam",
                color = Color.White,
                style = MaterialTheme.typography.labelLarge
            )
        }

        Spacer(modifier = Modifier.height(12.dp))

        OutlinedButton(
            onClick = {
                preferencesManager.clearSteamId()
                navController.navigate(Screen.Account.route) {
                    popUpTo(navController.graph.startDestinationId) { inclusive = true }
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp),
            shape = RoundedCornerShape(12.dp),
            border = BorderStroke(1.dp, Color.White),
            colors = ButtonDefaults.outlinedButtonColors(contentColor = Color.White)
        ) {
            Text(
                text = "Сменить аккаунт",
                style = MaterialTheme.typography.labelLarge
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