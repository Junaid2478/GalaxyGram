package com.example.galaxygram.feature.apod.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.galaxygram.data.apod.NasaApi
import com.example.galaxygram.data.model.ApodDto
import com.example.galaxygram.feature.list.model.HomeItem
import com.example.galaxygram.feature.list.model.HomeState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale
import java.util.TimeZone
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val api: NasaApi
) : ViewModel() {

    private val _state = MutableStateFlow(HomeState())
    val state: StateFlow<HomeState> = _state

    init { refresh() }

    fun refresh() = viewModelScope.launch {
        _state.update { it.copy(loading = true, error = null) }
        try {
            val end = dateString(0)
            val start = dateString(9)

            val dtos: List<ApodDto> = api.getApodRange(
                startDate = start,
                endDate = end,
                thumbs = true
            )

            val items = dtos.asReversed().mapIndexed { idx, dto ->
                val thumb = when {
                    !dto.thumbnail_url.isNullOrBlank() -> dto.thumbnail_url
                    isYouTube(dto.url) -> youTubeThumb(dto.url!!)
                    else -> null
                }
                val isVideo = dto.media_type == "video"
                if (idx < 3) {
                    android.util.Log.d("APOD",
                        "date=${dto.date} media=${dto.media_type} url=${dto.url} thumb=${dto.thumbnail_url} chosenThumb=$thumb")
                }
                android.util.Log.d(
                    "APOD",
                    "date=${dto.date} media=${dto.media_type} url=${dto.url} thumb=${dto.thumbnail_url}"
                )

                HomeItem(
                    date = dto.date,
                    title = dto.title,
                    explanation = dto.explanation,
                    imageUrl = if (isVideo) thumb else dto.url,
                    hdUrl = if (isVideo) null else dto.hdurl,
                    isVideo = isVideo,
                    videoUrl = if (isVideo) dto.url else null
                )
            }


            _state.update { it.copy(items = items, loading = false, error = null) }
        } catch (t: Throwable) {
            _state.update { it.copy(loading = false, error = t.message ?: "Failed to load APODs") }
        }
    }

    fun onPickDate() {
        // hook a date picker later; then call a fetch for that date
    }

    private fun dateString(daysAgo: Int): String {
        val cal = Calendar.getInstance(TimeZone.getTimeZone("UTC"))
        cal.add(Calendar.DAY_OF_YEAR, -daysAgo)
        val fmt = SimpleDateFormat("yyyy-MM-dd", Locale.US)
        fmt.timeZone = TimeZone.getTimeZone("UTC")
        return fmt.format(cal.time)
    }

    private fun isYouTube(url: String?): Boolean =
        url?.contains("youtube.com/watch") == true || url?.contains("youtu.be/") == true

    private fun youTubeThumb(src: String): String? {
        val uri = android.net.Uri.parse(src)
        val id = when {
            src.contains("youtube.com/watch") -> uri.getQueryParameter("v")
            src.contains("youtu.be/") -> src.substringAfter("youtu.be/").substringBefore("?")
            else -> null
        }
        return id?.let { "https://img.youtube.com/vi/$it/hqdefault.jpg" }
    }
}
