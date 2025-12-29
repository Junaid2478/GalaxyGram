package com.example.galaxygram.data.apod.repository

import com.example.galaxygram.data.apod.NasaApi
import com.example.galaxygram.data.apod.mapper.ApodMapper
import com.example.galaxygram.data.model.ApodDto
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.runTest
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory

class ApodRepositoryImplTest {

    private lateinit var mockWebServer: MockWebServer
    private lateinit var api: NasaApi
    private lateinit var repository: ApodRepositoryImpl
    private val testDispatcher = StandardTestDispatcher()

    @Before
    fun setup() {
        mockWebServer = MockWebServer()
        mockWebServer.start()

        val moshi = Moshi.Builder()
            .add(KotlinJsonAdapterFactory())
            .build()

        api = Retrofit.Builder()
            .baseUrl(mockWebServer.url("/"))
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .build()
            .create(NasaApi::class.java)

        repository = ApodRepositoryImpl(
            api = api,
            mapper = ApodMapper(),
            ioDispatcher = testDispatcher
        )
    }

    @After
    fun tearDown() {
        mockWebServer.shutdown()
    }

    @Test
    fun `getApodRange returns success with mapped data`() = runTest(testDispatcher) {
        val json = """
            [
                {
                    "date": "2025-01-01",
                    "title": "Test Galaxy",
                    "explanation": "A test explanation",
                    "url": "https://example.com/image.jpg",
                    "hdurl": "https://example.com/hd.jpg",
                    "media_type": "image",
                    "thumbnail_url": null
                }
            ]
        """.trimIndent()

        mockWebServer.enqueue(MockResponse().setBody(json).setResponseCode(200))

        val result = repository.getApodRange("2025-01-01", "2025-01-01")

        assertTrue(result.isSuccess)
        val apods = result.getOrThrow()
        assertEquals(1, apods.size)
        assertEquals("Test Galaxy", apods[0].title)
        assertEquals("2025-01-01", apods[0].date)
    }

    @Test
    fun `getApodRange returns failure on network error`() = runTest(testDispatcher) {
        mockWebServer.enqueue(MockResponse().setResponseCode(500))

        val result = repository.getApodRange("2025-01-01", "2025-01-01")

        assertTrue(result.isFailure)
    }

    @Test
    fun `getApodByDate returns success with single item`() = runTest(testDispatcher) {
        val json = """
            [
                {
                    "date": "2025-01-15",
                    "title": "Single APOD",
                    "explanation": "Description",
                    "url": "https://example.com/img.jpg",
                    "hdurl": null,
                    "media_type": "image",
                    "thumbnail_url": null
                }
            ]
        """.trimIndent()

        mockWebServer.enqueue(MockResponse().setBody(json).setResponseCode(200))

        val result = repository.getApodByDate("2025-01-15")

        assertTrue(result.isSuccess)
        assertEquals("Single APOD", result.getOrThrow().title)
    }

    @Test
    fun `getApodByDate returns failure when no item found`() = runTest(testDispatcher) {
        mockWebServer.enqueue(MockResponse().setBody("[]").setResponseCode(200))

        val result = repository.getApodByDate("2025-01-15")

        assertTrue(result.isFailure)
        assertTrue(result.exceptionOrNull() is NoSuchElementException)
    }

    @Test
    fun `getApodRange handles video media type`() = runTest(testDispatcher) {
        val json = """
            [
                {
                    "date": "2025-01-02",
                    "title": "Space Video",
                    "explanation": "Video content",
                    "url": "https://youtube.com/watch?v=abc123",
                    "hdurl": null,
                    "media_type": "video",
                    "thumbnail_url": "https://example.com/thumb.jpg"
                }
            ]
        """.trimIndent()

        mockWebServer.enqueue(MockResponse().setBody(json).setResponseCode(200))

        val result = repository.getApodRange("2025-01-02", "2025-01-02")

        assertTrue(result.isSuccess)
        val apod = result.getOrThrow().first()
        assertTrue(apod.isVideo)
        assertEquals("https://youtube.com/watch?v=abc123", apod.videoUrl)
    }
}

