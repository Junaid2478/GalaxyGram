package com.example.galaxygram.feature.list

import com.example.galaxygram.domain.model.Apod
import com.example.galaxygram.domain.usecase.GetRecentApodsUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever
import java.io.IOException

@OptIn(ExperimentalCoroutinesApi::class)
class HomeViewModelTest {

    private val testDispatcher = StandardTestDispatcher()
    private lateinit var getRecentApods: GetRecentApodsUseCase

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        getRecentApods = mock()
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `initial state shows loading`() = runTest {
        whenever(getRecentApods.invoke(10)).thenReturn(Result.success(emptyList()))

        val viewModel = HomeViewModel(getRecentApods)

        assertTrue(viewModel.state.value.loading)
    }

    @Test
    fun `refresh success updates state with items`() = runTest {
        val apods = listOf(
            createTestApod("2025-01-01", "Galaxy One"),
            createTestApod("2025-01-02", "Galaxy Two")
        )
        whenever(getRecentApods.invoke(10)).thenReturn(Result.success(apods))

        val viewModel = HomeViewModel(getRecentApods)
        advanceUntilIdle()

        val state = viewModel.state.value
        assertFalse(state.loading)
        assertNull(state.error)
        assertEquals(2, state.items.size)
        assertEquals("Galaxy One", state.items[0].title)
    }

    @Test
    fun `refresh failure updates state with error`() = runTest {
        whenever(getRecentApods.invoke(10)).thenReturn(
            Result.failure(IOException("Network error"))
        )

        val viewModel = HomeViewModel(getRecentApods)
        advanceUntilIdle()

        val state = viewModel.state.value
        assertFalse(state.loading)
        assertEquals("Network error", state.error)
        assertTrue(state.items.isEmpty())
    }

    @Test
    fun `refresh after error clears error and loads data`() = runTest {
        whenever(getRecentApods.invoke(10))
            .thenReturn(Result.failure(IOException("Network error")))
            .thenReturn(Result.success(listOf(createTestApod("2025-01-01", "Galaxy"))))

        val viewModel = HomeViewModel(getRecentApods)
        advanceUntilIdle()

        assertEquals("Network error", viewModel.state.value.error)

        viewModel.refresh()
        advanceUntilIdle()

        val state = viewModel.state.value
        assertNull(state.error)
        assertEquals(1, state.items.size)
    }

    private fun createTestApod(date: String, title: String) = Apod(
        date = date,
        title = title,
        explanation = "Test explanation",
        imageUrl = "https://example.com/image.jpg",
        hdUrl = "https://example.com/hd.jpg",
        isVideo = false,
        videoUrl = null,
        thumbnailUrl = null
    )
}

