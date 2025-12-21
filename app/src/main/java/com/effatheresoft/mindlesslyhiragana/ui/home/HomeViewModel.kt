package com.effatheresoft.mindlesslyhiragana.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.effatheresoft.mindlesslyhiragana.data.model.HiraganaCategory
import com.effatheresoft.mindlesslyhiragana.data.model.HiraganaCategory.HIMIKASE
import com.effatheresoft.mindlesslyhiragana.data.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

data class HomeUiState(
    val progress: String = HIMIKASE.id,
    val isLoading: Boolean = false,
    val unlockedCategories: List<HiraganaCategory> = emptyList(),
    val lockedCategories: List<HiraganaCategory> = emptyList(),
    val isResetDialogOpen: Boolean = false
)

@HiltViewModel
class HomeViewModel @Inject constructor(val userRepository: UserRepository) : ViewModel() {

    private val hiraganaCategories = HiraganaCategory.entries

    private val _localUser = userRepository.observeLocalUser()
    private val _isLoading = MutableStateFlow(false)
    private val _isResetDialogOpen = MutableStateFlow(false)

    val uiState: StateFlow<HomeUiState> = combine(_localUser, _isLoading, _isResetDialogOpen) { localUser, isLoading, isResetDialogOpen ->
        val currentProgressCategoryIndex = hiraganaCategories.indexOfFirst { it.id == localUser.progress }

        HomeUiState(
            progress = localUser.progress,
            isLoading = isLoading,
            unlockedCategories = hiraganaCategories.take(currentProgressCategoryIndex + 1),
            lockedCategories = hiraganaCategories.drop(currentProgressCategoryIndex + 1),
            isResetDialogOpen = isResetDialogOpen
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000L),
        initialValue = HomeUiState(isLoading = true)
    )

    fun onDrawerResetButtonClick() {
        _isResetDialogOpen.value = true
    }

    fun onResetDialogDismiss() {
        _isResetDialogOpen.value = false
    }

    fun onResetDialogConfirm() = viewModelScope.launch {
        userRepository.updateLocalUserProgress(HIMIKASE.id)
        userRepository.updateLocalUserLearningSetsCount(5)
        userRepository.updateLocalUserIsTestUnlocked(false)
        _isResetDialogOpen.value = false
    }
}