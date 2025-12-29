package com.example.galaxygram.feature.list

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.galaxygram.core.ui.components.ApodCard
import com.example.galaxygram.core.ui.components.HomeTopBar
import com.example.galaxygram.domain.model.Apod

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun HomeScreen(
    onOpenDetail: (Apod) -> Unit,
    viewModel: HomeViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsState()

    Scaffold(
        topBar = {
            HomeTopBar(
                onPickDate = { viewModel.onPickDate() },
                onRefresh = { viewModel.refresh() }
            )
        }
    ) { padding ->
        Box(Modifier.fillMaxSize().padding(padding)) {

            when {
                state.loading && state.items.isEmpty() -> {
                    CircularProgressIndicator(Modifier.align(Alignment.Center))
                }

                state.error != null && state.items.isEmpty() -> {
                    Column(
                        modifier = Modifier.align(Alignment.Center),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(state.error ?: "Error")
                        Spacer(Modifier.height(8.dp))
                        Button(onClick = { viewModel.refresh() }) { Text("Retry") }
                    }
                }

                else -> {
                    LazyVerticalStaggeredGrid(
                        columns = StaggeredGridCells.Adaptive(200.dp),
                        contentPadding = PaddingValues(12.dp),
                        verticalItemSpacing = 12.dp,
                        horizontalArrangement = Arrangement.spacedBy(12.dp),
                        modifier = Modifier.fillMaxSize()
                    ) {
                        items(state.items, key = { it.date }) { apod ->
                            ApodCard(
                                title = apod.title,
                                date = apod.date,
                                imageUrl = apod.imageUrl,
                                isVideo = apod.isVideo,
                                modifier = Modifier,
                                onClick = { onOpenDetail(apod) }
                            )
                        }
                    }
                }
            }
        }
    }
}
