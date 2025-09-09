package com.example.galaxygram.data.model

import com.squareup.moshi.Json

data class Apod(
    val date: String,
    val title: String,
    val explanation: String,
    val url: String,
    val hdurl: String? = null,
    @Json(name = "media_type") val mediaType: String
)
