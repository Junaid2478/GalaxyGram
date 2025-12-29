package com.example.galaxygram.feature.list.model

import com.example.galaxygram.domain.model.Apod

data class HomeState(
    val items: List<Apod> = emptyList(),
    val loading: Boolean = true,
    val error: String? = null
)
