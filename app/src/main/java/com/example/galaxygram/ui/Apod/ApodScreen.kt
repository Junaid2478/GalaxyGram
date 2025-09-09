package com.example.skeletonapp.ui.apod

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.example.galaxygram.data.model.Apod
import com.example.galaxygram.ui.Apod.ApodViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ApodScreen(
    vm: ApodViewModel = hiltViewModel()
) {
    val state by vm.uiState.collectAsState()
    var selected by remember { mutableStateOf<Apod?>(null) }

    LaunchedEffect(Unit) { vm.loadApods(10) }

    Scaffold(topBar = { TopAppBar(title = { Text("GalaxyGram") }) }) { padding ->
        Box(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
        ) {
            when (state) {
                is ApodUiState.Loading -> CircularProgressIndicator(Modifier.align(Alignment.Center))

                is ApodUiState.Error -> Text(
                    text = (state as ApodUiState.Error).message,
                    modifier = Modifier.align(Alignment.Center)
                )

                is ApodUiState.Success -> {
                    val items = (state as ApodUiState.Success).items
                    LazyColumn(
                        modifier = Modifier.fillMaxSize().padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        items(items) { apod ->
                            Card(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable { selected = apod }
                            ) {
                                Column(Modifier.padding(12.dp)) {
                                    AsyncImage(
                                        model = apod.url,
                                        contentDescription = apod.title,
                                        modifier = Modifier.fillMaxWidth().height(200.dp)
                                    )
                                    Spacer(Modifier.height(8.dp))
                                    Text(apod.title, style = MaterialTheme.typography.titleMedium)
                                }
                            }
                        }
                    }
                }

                else -> {}
            }
            if (selected != null) {
                AlertDialog(
                    onDismissRequest = { selected = null },
                    confirmButton = {
                        TextButton(onClick = { selected = null }) { Text("Close") }
                    },
                    title = { Text(selected!!.title) },
                    text = { Text(selected!!.explanation) }
                )
            }
        }
    }
}

