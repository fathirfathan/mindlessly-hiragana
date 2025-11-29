package com.effatheresoft.mindlesslyhiragana.learn

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.effatheresoft.mindlesslyhiragana.data.Hiragana
import com.effatheresoft.mindlesslyhiragana.data.HiraganaCategory
import com.effatheresoft.mindlesslyhiragana.data.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

data class LearnUiState(
    val isLoading: Boolean = false,
    val learningSetsCount: Int = 5
)

@HiltViewModel
class LearnViewModel @Inject constructor(val userRepository: UserRepository) : ViewModel() {

    private val _localUser = userRepository.observeLocalUser()
    private val _isLoading = MutableStateFlow(false)

    val uiState: StateFlow<LearnUiState> = combine(_localUser, _isLoading) { localUser, isLoading ->
        LearnUiState(
            isLoading = isLoading,
            learningSetsCount = localUser.learningSetsCount
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000L),
        initialValue = LearnUiState(isLoading = true)
    )

    fun updateLearningSetsCount(count: Int) {
        viewModelScope.launch {
            _isLoading.value = true
            userRepository.updateLocalUserLearningSetsCount(count)
            _isLoading.value = false
        }
    }
}