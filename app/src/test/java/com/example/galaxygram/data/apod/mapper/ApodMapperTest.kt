package com.example.galaxygram.data.apod.mapper

import com.example.galaxygram.data.model.ApodDto
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class ApodMapperTest {

    private lateinit var mapper: ApodMapper

    @Before
    fun setup() {
        mapper = ApodMapper()
    }

    @Test
    fun `mapToDomain maps image correctly`() {
        val dto = ApodDto(
            date = "2025-01-01",
            title = "Nebula",
            explanation = "A beautiful nebula",
            url = "https://example.com/image.jpg",
            hdurl = "https://example.com/hd.jpg",
            media_type = "image",
            thumbnail_url = null
        )

        val result = mapper.mapToDomain(dto)

        assertEquals("2025-01-01", result.date)
        assertEquals("Nebula", result.title)
        assertEquals("A beautiful nebula", result.explanation)
        assertEquals("https://example.com/image.jpg", result.imageUrl)
        assertEquals("https://example.com/hd.jpg", result.hdUrl)
        assertFalse(result.isVideo)
        assertNull(result.videoUrl)
    }

    @Test
    fun `mapToDomain maps video with thumbnail correctly`() {
        val dto = ApodDto(
            date = "2025-01-02",
            title = "Space Video",
            explanation = "Video about space",
            url = "https://youtube.com/watch?v=abc123",
            hdurl = null,
            media_type = "video",
            thumbnail_url = "https://example.com/thumb.jpg"
        )

        val result = mapper.mapToDomain(dto)

        assertTrue(result.isVideo)
        assertEquals("https://youtube.com/watch?v=abc123", result.videoUrl)
        assertEquals("https://example.com/thumb.jpg", result.imageUrl)
        assertEquals("https://example.com/thumb.jpg", result.thumbnailUrl)
        assertNull(result.hdUrl)
    }

    @Test
    fun `mapToDomain extracts YouTube thumbnail when no thumbnail provided`() {
        val dto = ApodDto(
            date = "2025-01-03",
            title = "YouTube Video",
            explanation = "A YouTube video",
            url = "https://youtube.com/watch?v=dQw4w9WgXcQ",
            hdurl = null,
            media_type = "video",
            thumbnail_url = null
        )

        val result = mapper.mapToDomain(dto)

        assertTrue(result.isVideo)
        assertEquals("https://img.youtube.com/vi/dQw4w9WgXcQ/hqdefault.jpg", result.thumbnailUrl)
        assertEquals("https://img.youtube.com/vi/dQw4w9WgXcQ/hqdefault.jpg", result.imageUrl)
    }

    @Test
    fun `mapToDomain extracts YouTube thumbnail from short URL`() {
        val dto = ApodDto(
            date = "2025-01-04",
            title = "Short URL Video",
            explanation = "Video with short URL",
            url = "https://youtu.be/abc123",
            hdurl = null,
            media_type = "video",
            thumbnail_url = null
        )

        val result = mapper.mapToDomain(dto)

        assertEquals("https://img.youtube.com/vi/abc123/hqdefault.jpg", result.thumbnailUrl)
    }

    @Test
    fun `mapToDomainList maps multiple items`() {
        val dtos = listOf(
            ApodDto("2025-01-01", "One", "Exp", "url1", null, "image", null),
            ApodDto("2025-01-02", "Two", "Exp", "url2", null, "image", null)
        )

        val result = mapper.mapToDomainList(dtos)

        assertEquals(2, result.size)
        assertEquals("One", result[0].title)
        assertEquals("Two", result[1].title)
    }

    @Test
    fun `mapToDomain handles null url gracefully`() {
        val dto = ApodDto(
            date = "2025-01-05",
            title = "No URL",
            explanation = "Missing URL",
            url = null,
            hdurl = null,
            media_type = "image",
            thumbnail_url = null
        )

        val result = mapper.mapToDomain(dto)

        assertNull(result.imageUrl)
        assertNull(result.hdUrl)
    }
}

