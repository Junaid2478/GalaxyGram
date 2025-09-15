package com.example.galaxygram.data.model

data class ApodDto(
    val date: String,
    val title: String,
    val explanation: String,
    val url: String?,
    val hdurl: String?,
    val media_type: String,
    val thumbnail_url: String?
)
