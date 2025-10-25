package com.effatheresoft.mindlesslyhiragana.ui.home

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.effatheresoft.mindlesslyhiragana.data.User
import com.effatheresoft.mindlesslyhiragana.data.UserInteraction
import com.effatheresoft.mindlesslyhiragana.data.UserRepository
import com.effatheresoft.mindlesslyhiragana.util.Result
import com.effatheresoft.mindlesslyhiragana.ui.home.HomeUiState.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

sealed class HomeUiState {
    data object Loading: HomeUiState()
    data class Success(
        val highestCategoryId: String,
        val isDrawerOpen: Boolean,
        val isRestartDialogShown: Boolean
    ): HomeUiState()
    data class Error(val exception: Throwable): HomeUiState()
}

class HomeViewModel(
    private val userRepository: UserRepository
): ViewModel() {
    private val _uiState = MutableStateFlow<HomeUiState>(Loading)
    val uiState = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            userRepository.getDefaultUser().collect {
                when (it) {
                    is Result.Loading -> _uiState.value = Loading
                    is Result.Success -> {
                        if (it.data == null) {
                            createDefaultUser()
                        } else {
                            _uiState.value = Success(
                                highestCategoryId = it.data.highestCategoryId,
                                isDrawerOpen = false,
                                isRestartDialogShown = false
                            )
                        }
                    }
                    is Result.Error -> {
                        _uiState.value = Error(it.exception)
                        Log.d("HomeViewModel", "Error: ${it.exception}")
                    }
                }
            }
        }
    }

    fun createDefaultUser() {
        userRepository.insertUser(User(id = "1", highestCategoryId = "0", learningSetsCount = 3)).onEach {
            when (it) {
                is Result.Loading -> {}
                is Result.Success -> _uiState.value = Success(
                    highestCategoryId = "0",
                    isDrawerOpen = false,
                    isRestartDialogShown = false
                )
                is Result.Error -> {
                    _uiState.value = Error(it.exception)
                    Log.d("HomeViewModel", "Error: ${it.exception}")
                }
            }
        }.launchIn(viewModelScope)
    }

    fun toggleRestartDialog() {
        if (_uiState.value is Success) {
            recordInteraction("Click", "Home:Drawer:Restart:${if ((_uiState.value as Success).isRestartDialogShown) "Closed" else "Open"}")

            _uiState.value = Success(
                highestCategoryId = (_uiState.value as Success).highestCategoryId,
                isDrawerOpen = (_uiState.value as Success).isDrawerOpen,
                isRestartDialogShown = !(_uiState.value as Success).isRestartDialogShown
            )
        }
    }

    fun toggleDrawer() {
        if (_uiState.value is Success) {
            recordInteraction("Click Swipe", "Home:Drawer:${if ((_uiState.value as Success).isDrawerOpen) "Closed" else "Open"}")

            _uiState.value = Success(
                highestCategoryId = (_uiState.value as Success).highestCategoryId,
                isDrawerOpen = !(_uiState.value as Success).isDrawerOpen,
                isRestartDialogShown = (_uiState.value as Success).isRestartDialogShown
            )
        }
    }

    fun restartProgress() {
        viewModelScope.launch {
            _uiState.value = Loading
            userRepository.restartProgress()
        }.invokeOnCompletion {
            recordInteraction("Click", "Home:Drawer:Restart:Dialog:Confirm")

            _uiState.value = Success(
                highestCategoryId = "0",
                isDrawerOpen = false,
                isRestartDialogShown = false
            )
        }
    }

    fun onCategoryClicked(categoryId: String, onNavigateToDetails: (String) -> Unit = {}) {
        recordInteraction("Click", "Home:Category:$categoryId")

        onNavigateToDetails(categoryId)
    }

    fun recordInteraction(event: String, target: String) {
        val formatter = SimpleDateFormat("yyyy:MM:dd:HH:mm:ss", Locale.getDefault())
        userRepository.recordInteraction(
            UserInteraction(
                formatter.format(Date()),
                event,
                target
            )
        )
    }
}


