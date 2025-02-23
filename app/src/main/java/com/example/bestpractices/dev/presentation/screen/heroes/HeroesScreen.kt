package com.example.bestpractices.dev.presentation.screen.heroes

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.bestpractices.dev.domain.model.Heroes
import com.example.bestpractices.dev.presentation.viewmodel.HeroesViewModel

@Composable
fun HeroesScreen(
    navController: NavController,
    viewModel: HeroesViewModel = hiltViewModel()
) {
    val heroesState by viewModel.heroesState

    LaunchedEffect(Unit) {
        viewModel.loadHeroes()
    }

    when (val state = heroesState) {
        is HeroesState.Idle -> {
        }
        is HeroesState.Loading -> {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        }
        is HeroesState.Success -> {
            LazyColumn {
                items(state.heroes) { hero ->
                    HeroItem(hero = hero)
                }
            }
        }
        is HeroesState.Error -> {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text(text = "Error: ${state.message}")
            }
        }
    }
}

@Composable
fun HeroItem(hero: Heroes) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .shadow(elevation = 4.dp)
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth()
        ) {
            AsyncImage(
                model = hero.logo,
                contentDescription = null,
                modifier = Modifier
                    .size(64.dp)
                    .clip(RoundedCornerShape(8.dp)),
                contentScale = ContentScale.Crop
            )

            Spacer(modifier = Modifier.width(16.dp))

            Column {
                Text(
                    text = hero.localizedName ?: "Unknown",
                    fontWeight = FontWeight.Bold
                )
                Text(text = "Primary Attribute: ${hero.primaryAttr}")
                Text(text = "Attack Type: ${hero.attackType}")
                Text(text = "Roles: ${hero.roles.joinToString(", ")}")
            }
        }
    }
}