package com.example.skeletonapp.ui.apod

import com.example.skeletonapp.data.repo.ApodRepo
import com.example.galaxygram.ui.Apod.ApodViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.*
import org.junit.*
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever
import java.io.IOException

@OptIn(ExperimentalCoroutinesApi::class)
class ApodViewModelTest {

    private val testDispatcher = StandardTestDispatcher()

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `loadApods emits Success on success`() = runTest {
        val repo = mock<ApodRepo>()
        whenever(repo.fetchApodList(1)).thenReturn(
            Result.success(
                listOf(
                    Apod(
                        date = "2025-09-09",
                        title = "Test Galaxy",
                        explanation = "A beautiful test galaxy.",
                        url = "https://example.com/image.jpg",
                        mediaType = "image"
                    )
                )
            )
        )

        val vm = ApodViewModel(repo)
        vm.loadApods(1)
        advanceUntilIdle()

        val state = vm.uiState.value
        Assert.assertTrue(state is ApodUiState.Success)
        Assert.assertEquals(1, (state as ApodUiState.Success).items.size)
    }

    @Test
    fun `loadApods emits Error on failure`() = runTest {
        val repo = mock<ApodRepo>()
        whenever(repo.fetchApodList(1)).thenReturn(Result.failure(IOException("Network down")))

        val vm = ApodViewModel(repo)
        vm.loadApods(1)
        advanceUntilIdle()

        val state = vm.uiState.value
        Assert.assertTrue(state is ApodUiState.Error)
        Assert.assertEquals("Network down", (state as ApodUiState.Error).message)
    }
}

