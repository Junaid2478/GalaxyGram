package com.example.skeletonapp.data.repo

import com.example.galaxygram.BuildConfig
import com.example.galaxygram.data.api.NasaApi
import com.example.galaxygram.data.model.Apod
import javax.inject.Inject


class ApodRepo @Inject constructor(
    private val api: NasaApi
) {
    suspend fun fetchApodList(count: Int = 20): Result<List<Apod>> = runCatching {
        val res = api.getApodList(BuildConfig.NASA_KEY, count)
        if (!res.isSuccessful) error("HTTP ${res.code()}")
        val list = res.body().orEmpty()
        list.filter { it.mediaType == "image" }
    }

    suspend fun fetchApodByDate(date: String): Result<Apod> = runCatching {
        val res = api.getApodByDate(BuildConfig.NASA_KEY, date)
        if (!res.isSuccessful) error("HTTP ${res.code()}")
        res.body() ?: error("Empty body")
    }
}
