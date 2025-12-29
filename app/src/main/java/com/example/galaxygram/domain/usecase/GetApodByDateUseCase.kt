package com.example.galaxygram.domain.usecase

import com.example.galaxygram.domain.model.Apod
import com.example.galaxygram.domain.repository.ApodRepository
import javax.inject.Inject

class GetApodByDateUseCase @Inject constructor(
    private val repository: ApodRepository
) {
    suspend operator fun invoke(date: String): Result<Apod> {
        return repository.getApodByDate(date)
    }
}
