package com.example.galaxygram.data.apod.mapper

import com.example.galaxygram.data.model.ApodDto
import com.example.galaxygram.domain.model.Apod
import javax.inject.Inject

class ApodMapper @Inject constructor() {
    
    fun mapToDomain(dto: ApodDto): Apod {
        val isVideo = dto.media_type == "video"
        val thumbnail = resolveThumbnail(dto)
        
        return Apod(
            date = dto.date,
            title = dto.title,
            explanation = dto.explanation,
            imageUrl = if (isVideo) thumbnail else dto.url,
            hdUrl = if (isVideo) null else dto.hdurl,
            isVideo = isVideo,
            videoUrl = if (isVideo) dto.url else null,
            thumbnailUrl = thumbnail
        )
    }
    
    fun mapToDomainList(dtos: List<ApodDto>): List<Apod> {
        return dtos.map { mapToDomain(it) }
    }
    
    private fun resolveThumbnail(dto: ApodDto): String? {
        return when {
            !dto.thumbnail_url.isNullOrBlank() -> dto.thumbnail_url
            isYouTubeUrl(dto.url) -> extractYouTubeThumbnail(dto.url!!)
            else -> null
        }
    }
    
    private fun isYouTubeUrl(url: String?): Boolean {
        return url?.let {
            it.contains("youtube.com/watch") || it.contains("youtu.be/")
        } ?: false
    }
    
    private fun extractYouTubeThumbnail(url: String): String? {
        val videoId = when {
            url.contains("youtube.com/watch") -> extractQueryParam(url, "v")
            url.contains("youtu.be/") -> url.substringAfter("youtu.be/").substringBefore("?").substringBefore("/")
            else -> null
        }
        return videoId?.takeIf { it.isNotBlank() }?.let { 
            "https://img.youtube.com/vi/$it/hqdefault.jpg" 
        }
    }
    
    private fun extractQueryParam(url: String, param: String): String? {
        val queryStart = url.indexOf('?')
        if (queryStart == -1) return null
        
        val query = url.substring(queryStart + 1)
        return query.split('&')
            .map { it.split('=', limit = 2) }
            .find { it.size == 2 && it[0] == param }
            ?.get(1)
    }
}
