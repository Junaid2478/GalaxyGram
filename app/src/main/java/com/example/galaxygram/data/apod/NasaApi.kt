package com.example.galaxygram.data.apod

import com.example.galaxygram.BuildConfig
import com.example.galaxygram.data.model.ApodDto
import retrofit2.http.GET
import retrofit2.http.Query

interface NasaApi {
    @GET("planetary/apod")
    suspend fun getApodRange(
        @Query("start_date") startDate: String,
        @Query("end_date") endDate: String,
        @Query("thumbs") thumbs: Boolean = true,
        @Query("api_key") apiKey: String = BuildConfig.NASA_KEY
    ): List<ApodDto>
}