package com.example.galaxygram.data.apod.mapper

import android.net.Uri
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
        val uri = Uri.parse(url)
        val videoId = when {
            url.contains("youtube.com/watch") -> uri.getQueryParameter("v")
            url.contains("youtu.be/") -> url.substringAfter("youtu.be/").substringBefore("?")
            else -> null
        }
        return videoId?.let { "https://img.youtube.com/vi/$it/hqdefault.jpg" }
    }
}
