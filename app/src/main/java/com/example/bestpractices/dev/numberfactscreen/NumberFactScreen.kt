package com.example.bestpractices.dev.numberfactscreen

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.bestpractices.dev.MainViewModel
import com.example.bestpractices.dev.MainViewModelFactory
import com.example.bestpractices.dev.NumberFactIntent
import com.example.bestpractices.dev.NumberFactResponse
import com.example.bestpractices.dev.NumberFactState

@Composable
fun NumberFactScreen(viewModel: MainViewModel = viewModel(factory = MainViewModelFactory())) {
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
fun FactView(factResponse: NumberFactResponse) {
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