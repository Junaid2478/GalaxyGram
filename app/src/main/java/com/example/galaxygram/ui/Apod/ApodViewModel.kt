package com.example.galaxygram.ui.Apod

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.skeletonapp.data.repo.ApodRepo
import com.example.skeletonapp.ui.apod.ApodUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ApodViewModel @Inject constructor(
    private val repo: ApodRepo
) : ViewModel() {

    private val _uiState = MutableStateFlow<ApodUiState>(ApodUiState.Loading)
    val uiState: StateFlow<ApodUiState> = _uiState

    fun loadApods(count: Int = 10) {
        _uiState.value = ApodUiState.Loading
        viewModelScope.launch {
            val result = repo.fetchApodList(count)
            _uiState.value = result.fold(
                onSuccess = { ApodUiState.Success(it) },
                onFailure = { ApodUiState.Error(it.message ?: "Unknown error") }
            )
        }
    }
}
