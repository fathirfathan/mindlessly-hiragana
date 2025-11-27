package com.effatheresoft.mindlesslyhiragana.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.effatheresoft.mindlesslyhiragana.data.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

data class HomeUiState(
    val progress: String = "himikase",
    val isLoading: Boolean = false
)

@HiltViewModel
class HomeViewModel @Inject constructor(val userRepository: UserRepository) : ViewModel() {

    private val _localUser = userRepository.getLocalUser()
    private val _isLoading = MutableStateFlow(false)

    val uiState: StateFlow<HomeUiState> = combine(_localUser, _isLoading) { localUser, isLoading ->
        HomeUiState(
            progress = localUser.progress,
            isLoading = isLoading
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000L),
        initialValue = HomeUiState(isLoading = true)
    )
}