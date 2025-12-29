package com.example.galaxygram.domain.usecase

import com.example.galaxygram.domain.model.Apod
import com.example.galaxygram.domain.repository.ApodRepository
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.mockito.kotlin.any
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever
import java.io.IOException

class GetRecentApodsUseCaseTest {

    private lateinit var repository: ApodRepository
    private lateinit var useCase: GetRecentApodsUseCase

    @Before
    fun setup() {
        repository = mock()
        useCase = GetRecentApodsUseCase(repository)
    }

    @Test
    fun `invoke returns sorted apods most recent first`() = runTest {
        val apods = listOf(
            createTestApod("2025-01-01"),
            createTestApod("2025-01-03"),
            createTestApod("2025-01-02")
        )
        whenever(repository.getApodRange(any(), any())).thenReturn(Result.success(apods))

        val result = useCase(daysBack = 10)

        assertTrue(result.isSuccess)
        val sorted = result.getOrThrow()
        assertEquals("2025-01-03", sorted[0].date)
        assertEquals("2025-01-02", sorted[1].date)
        assertEquals("2025-01-01", sorted[2].date)
    }

    @Test
    fun `invoke with default daysBack calls repository with 10 days`() = runTest {
        whenever(repository.getApodRange(any(), any())).thenReturn(Result.success(emptyList()))

        useCase()

        verify(repository).getApodRange(any(), any())
    }

    @Test
    fun `invoke returns failure when repository fails`() = runTest {
        whenever(repository.getApodRange(any(), any())).thenReturn(
            Result.failure(IOException("API error"))
        )

        val result = useCase()

        assertTrue(result.isFailure)
        assertEquals("API error", result.exceptionOrNull()?.message)
    }

    @Test
    fun `invoke returns empty list when repository returns empty`() = runTest {
        whenever(repository.getApodRange(any(), any())).thenReturn(Result.success(emptyList()))

        val result = useCase()

        assertTrue(result.isSuccess)
        assertTrue(result.getOrThrow().isEmpty())
    }

    private fun createTestApod(date: String) = Apod(
        date = date,
        title = "Test",
        explanation = "Test",
        imageUrl = null,
        hdUrl = null,
        isVideo = false,
        videoUrl = null,
        thumbnailUrl = null
    )
}

