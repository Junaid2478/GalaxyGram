package com.example.galaxygram.domain.repository

import com.example.galaxygram.domain.model.Apod

interface ApodRepository {
    suspend fun getApodRange(startDate: String, endDate: String): Result<List<Apod>>
    suspend fun getApodByDate(date: String): Result<Apod>
}
