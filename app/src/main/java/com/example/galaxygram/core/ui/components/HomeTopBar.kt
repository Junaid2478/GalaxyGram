package com.example.galaxygram.core.ui.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeTopBar(
    onPickDate: () -> Unit,
    onRefresh: () -> Unit
) {
    TopAppBar(
        title = { Text("GalaxyGram") },
        actions = {
            IconButton(onClick = onPickDate) {
                Icon(Icons.Default.DateRange, contentDescription = "Pick date")
            }
            IconButton(onClick = onRefresh) {
                Icon(Icons.Default.Refresh, contentDescription = "Refresh")
            }
        }
    )
}