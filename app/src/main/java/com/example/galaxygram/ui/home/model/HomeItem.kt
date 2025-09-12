package com.example.galaxygram.ui.home.model

data class HomeItem(
    val date: String,
    val title: String,
    val explanation: String,
    val imageUrl: String?,
    val hdUrl: String?,
    val isVideo: Boolean
)