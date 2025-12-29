package com.example.galaxygram.domain.model

data class Apod(
    val date: String,
    val title: String,
    val explanation: String,
    val imageUrl: String?,
    val hdUrl: String?,
    val isVideo: Boolean,
    val videoUrl: String?,
    val thumbnailUrl: String?
)
