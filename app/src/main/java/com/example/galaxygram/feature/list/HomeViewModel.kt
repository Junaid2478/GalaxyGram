package com.example.galaxygram.feature.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.galaxygram.domain.usecase.GetRecentApodsUseCase
import com.example.galaxygram.feature.list.model.HomeState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val getRecentApods: GetRecentApodsUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(HomeState())
    val state: StateFlow<HomeState> = _state

    init {
        refresh()
    }

    fun refresh() = viewModelScope.launch {
        _state.update { it.copy(loading = true, error = null) }
        
        getRecentApods(daysBack = 10)
            .onSuccess { apods ->
                _state.update { 
                    it.copy(
                        items = apods, 
                        loading = false, 
                        error = null
                    ) 
                }
            }
            .onFailure { throwable ->
                _state.update { 
                    it.copy(
                        loading = false, 
                        error = throwable.message ?: "Failed to load APODs"
                    ) 
                }
            }
    }

    fun onPickDate() {
    }
}
