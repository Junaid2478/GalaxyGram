package com.example.galaxygram.feature.list

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

data class HomeState(
    val items: List<HomeItem> = emptyList(),
    val loading: Boolean = true,
    val error: String? = null
)

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
            val end = dateString(daysAgo = 0)
            val start = dateString(daysAgo = 19)
            val dtos: List<ApodDto> = api.getApodRange(
                startDate = start,
                endDate = end,
                thumbs = true
            )
            val items = dtos.asReversed().map { dto ->
                HomeItem(
                    date = dto.date,
                    title = dto.title,
                    explanation = dto.explanation,
                    imageUrl = dto.url ?: dto.thumbnail_url,
                    hdUrl = dto.hdurl,
                    isVideo = dto.media_type != "image"
                )
            }
            _state.update { it.copy(items = items, loading = false, error = null) }
        } catch (t: Throwable) {
            _state.update { it.copy(loading = false, error = t.message ?: "Failed to load APODs") }
        }
    }

    fun onPickDate() { /* TODO add a date picker later; then fetch that date */ }

    private fun dateString(daysAgo: Int): String {
        val cal = Calendar.getInstance(TimeZone.getTimeZone("UTC"))
        cal.add(Calendar.DAY_OF_YEAR, -daysAgo)
        val fmt = SimpleDateFormat("yyyy-MM-dd", Locale.US)
        fmt.timeZone = TimeZone.getTimeZone("UTC")
        return fmt.format(cal.time)
    }
}
