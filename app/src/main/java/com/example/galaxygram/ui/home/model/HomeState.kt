package com.example.galaxygram.ui.home.model

data class HomeState(
    val items: List<HomeItem> = emptyList(),
    val loading: Boolean = true,
    val isRefreshing: Boolean = false
)