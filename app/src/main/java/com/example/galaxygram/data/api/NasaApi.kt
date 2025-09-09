package com.example.galaxygram.data.api

import com.example.galaxygram.data.model.Apod
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface NasaApi {

    @GET("planetary/apod")
    suspend fun getApodList(
        @Query("api_key") apiKey: String,
        @Query("count") count: Int = 20
    ): Response<List<Apod>>

    @GET("planetary/apod")
    suspend fun getApodByDate(
        @Query("api_key") apiKey: String,
        @Query("date") date: String
    ): Response<Apod>
}

