package com.example.galaxygram.domain.usecase

import com.example.galaxygram.domain.model.Apod
import com.example.galaxygram.domain.repository.ApodRepository
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale
import java.util.TimeZone
import javax.inject.Inject

class GetRecentApodsUseCase @Inject constructor(
    private val repository: ApodRepository
) {
    suspend operator fun invoke(daysBack: Int = 10): Result<List<Apod>> {
        val endDate = getDateString(daysAgo = 0)
        val startDate = getDateString(daysAgo = daysBack - 1)
        
        return repository.getApodRange(startDate, endDate)
            .map { apods -> apods.sortedByDescending { it.date } }
    }
    
    private fun getDateString(daysAgo: Int): String {
        val calendar = Calendar.getInstance(TimeZone.getTimeZone("UTC"))
        calendar.add(Calendar.DAY_OF_YEAR, -daysAgo)
        
        val formatter = SimpleDateFormat("yyyy-MM-dd", Locale.US)
        formatter.timeZone = TimeZone.getTimeZone("UTC")
        
        return formatter.format(calendar.time)
    }
}
