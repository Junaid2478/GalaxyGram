package com.example.skeletonapp.ui.apod

import com.example.galaxygram.data.model.Apod

// Represents all possible screen states
sealed class ApodUiState {
    data object Loading : ApodUiState()
    data class Success(val items: List<Apod>) : ApodUiState()
    data class Error(val message: String) : ApodUiState()
}
