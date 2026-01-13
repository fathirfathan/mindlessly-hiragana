package com.effatheresoft.mindlesslyhiragana.ui.learn

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.effatheresoft.mindlesslyhiragana.Constants.DEFAULT_LEARNING_SETS_COUNT
import com.effatheresoft.mindlesslyhiragana.data.model.HiraganaCategory
import com.effatheresoft.mindlesslyhiragana.data.repository.UserRepository
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
    val category: HiraganaCategory? = null,
    val learningSetsCount: Int = DEFAULT_LEARNING_SETS_COUNT
)

@HiltViewModel
class LearnViewModel @Inject constructor(val userRepository: UserRepository) : ViewModel() {

    private val _localUser = userRepository.observeLocalUser()
    private val _isLoading = MutableStateFlow(false)

    val uiState: StateFlow<LearnUiState> = combine(_localUser, _isLoading) { localUser, isLoading ->
        LearnUiState(
            isLoading = isLoading,
            category = HiraganaCategory.entries.first { it.id == localUser.progress },
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