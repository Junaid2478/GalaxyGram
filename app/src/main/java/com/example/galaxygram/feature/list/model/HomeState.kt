package com.example.galaxygram.feature.list.model

data class HomeState(
    val items: List<HomeItem> = emptyList(),
    val loading: Boolean = true,
    val error: String? = null
)
