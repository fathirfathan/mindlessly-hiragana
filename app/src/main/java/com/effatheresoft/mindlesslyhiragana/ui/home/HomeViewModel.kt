package com.effatheresoft.mindlesslyhiragana.ui.home

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.effatheresoft.mindlesslyhiragana.data.User
import com.effatheresoft.mindlesslyhiragana.data.UserRepository
import com.effatheresoft.mindlesslyhiragana.util.Result
import com.effatheresoft.mindlesslyhiragana.ui.home.HomeUiState.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

sealed class HomeUiState {
    data object Loading: HomeUiState()
    data class Success(val highestCategoryId: String): HomeUiState()
    data class Error(val exception: Throwable): HomeUiState()
}

class HomeViewModel(
    private val userRepository: UserRepository
): ViewModel() {
    private val user = userRepository.getDefaultUser()
    private val _uiState = MutableStateFlow<HomeUiState>(Loading)
    val uiState = _uiState.asStateFlow()

    fun createDefaultUser() {
        userRepository.insertUser(User(id = "1", highestCategoryId = "0")).onEach {
            when (it) {
                is Result.Loading -> {}
                is Result.Success -> _uiState.value = Success(highestCategoryId = "0")
                is Result.Error -> {
                    _uiState.value = Error(it.exception)
                    Log.d("HomeViewModel", "Error: ${it.exception}")
                }
            }
        }.launchIn(viewModelScope)
    }

    init {
        user.onEach {
            when (it) {
                is Result.Loading -> _uiState.value = Loading
                is Result.Success -> {
                    if (it.data == null) {
                        createDefaultUser()
                    } else {
                        _uiState.value = Success(it.data.highestCategoryId)
                    }
                }
                is Result.Error -> {
                    _uiState.value = Error(it.exception)
                    Log.d("HomeViewModel", "Error: ${it.exception}")
                }
            }
        }.launchIn(viewModelScope)
    }
}


