package com.example.bestpractices.dev.presentation.screen.numberfactscreen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.bestpractices.dev.domain.model.NumberFact
import com.example.bestpractices.dev.presentation.viewmodel.MainViewModel

@Composable
fun NumberFactScreen(viewModel: MainViewModel = hiltViewModel()) {
    val state by viewModel.state.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        when (val currentState = state) {
            is NumberFactState.Idle -> IdleView()
            is NumberFactState.Loading -> LoadingView()
            is NumberFactState.Success -> FactView(currentState.factResponse)
            is NumberFactState.Error -> ErrorView(currentState.message)
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(onClick = { viewModel.processIntent(NumberFactIntent.FetchRandomFact) }) {
            Text("Get Random Fact")
        }
    }
}

@Composable
fun IdleView() {
    Text("Click button to get fact", style = MaterialTheme.typography.bodyLarge)
}

@Composable
fun LoadingView() {
    CircularProgressIndicator()
}

@Composable
fun FactView(factResponse: NumberFact) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Number: ${factResponse.number}",
            style = MaterialTheme.typography.headlineSmall,
            color = MaterialTheme.colorScheme.primary,
            modifier = Modifier.padding(bottom = 8.dp)
        )

        Text(
            text = "Type: ${factResponse.type.replaceFirstChar { it.uppercase() }}",
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.secondary,
            modifier = Modifier.padding(bottom = 8.dp)
        )

        Text(
            text = factResponse.text,
            style = MaterialTheme.typography.bodyLarge,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        Text(
            text = if (factResponse.found) "Fact found!" else "Fact not found",
            style = MaterialTheme.typography.labelMedium,
            color = if (factResponse.found) Color.Green else Color.Red
        )
    }
}

@Composable
fun ErrorView(message: String) {
    Text("Error: $message", style = MaterialTheme.typography.bodyLarge, color = Color.Red)
}