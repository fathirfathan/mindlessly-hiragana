package com.effatheresoft.mindlesslyhiragana.ui.home

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.effatheresoft.mindlesslyhiragana.data.HiraganaCategory
import com.effatheresoft.mindlesslyhiragana.data.HiraganaRepository
import com.effatheresoft.mindlesslyhiragana.util.Result
import com.effatheresoft.mindlesslyhiragana.ui.home.HomeUiState.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

sealed class HomeUiState {
    data object Loading: HomeUiState()
    data class Success(val hiraganaCategories: List<HiraganaCategory>): HomeUiState()
    data class Error(val exception: Throwable): HomeUiState()
}

class HomeViewModel(
    private val repository: HiraganaRepository
): ViewModel() {
    private val hiraganaCategories = repository.getHiraganaCategories()
    private val _uiState = MutableStateFlow<HomeUiState>(Loading)
    val uiState = _uiState.asStateFlow()

    init {
        hiraganaCategories.onEach {
            when (it) {
                is Result.Loading -> _uiState.value = Loading
                is Result.Success -> _uiState.value = Success(it.data)
                is Result.Error -> {
                    _uiState.value = Error(it.exception)
                    Log.d("HomeViewModel", "Error: ${it.exception}")
                }
            }
        }.launchIn(viewModelScope)
    }
}

