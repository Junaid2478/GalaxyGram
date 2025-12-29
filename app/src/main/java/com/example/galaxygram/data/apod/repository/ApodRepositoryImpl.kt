package com.example.galaxygram.data.apod.repository

import com.example.galaxygram.core.di.IoDispatcher
import com.example.galaxygram.data.apod.NasaApi
import com.example.galaxygram.data.apod.mapper.ApodMapper
import com.example.galaxygram.domain.model.Apod
import com.example.galaxygram.domain.repository.ApodRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ApodRepositoryImpl @Inject constructor(
    private val api: NasaApi,
    private val mapper: ApodMapper,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher
) : ApodRepository {
    
    override suspend fun getApodRange(startDate: String, endDate: String): Result<List<Apod>> {
        return withContext(ioDispatcher) {
            runCatching {
                val dtos = api.getApodRange(
                    startDate = startDate,
                    endDate = endDate,
                    thumbs = true
                )
                mapper.mapToDomainList(dtos)
            }
        }
    }
    
    override suspend fun getApodByDate(date: String): Result<Apod> {
        return withContext(ioDispatcher) {
            runCatching {
                val dtos = api.getApodRange(
                    startDate = date,
                    endDate = date,
                    thumbs = true
                )
                dtos.firstOrNull()?.let { mapper.mapToDomain(it) }
                    ?: throw NoSuchElementException("No APOD found for date: $date")
            }
        }
    }
}
