package com.effatheresoft.mindlesslyhiragana.ui.home

import androidx.compose.material3.DrawerState
import androidx.compose.material3.DrawerValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.effatheresoft.mindlesslyhiragana.data.model.HiraganaCategory
import com.effatheresoft.mindlesslyhiragana.data.model.HiraganaCategory.HIMIKASE
import com.effatheresoft.mindlesslyhiragana.data.repository.RefactoredUserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

data class HomeUiState(
    val isLoading: Boolean = false,
    val categories: List<HomeCategory> = emptyList(),
    val isResetDialogOpen: Boolean = false
)

@HiltViewModel
class HomeViewModel @Inject constructor(val userRepository: RefactoredUserRepository) : ViewModel() {

    private val _localUser = userRepository.observeLocalUser()
    private val _isLoading = MutableStateFlow(false)
    private val _isResetDialogOpen = MutableStateFlow(false)

    val uiState: StateFlow<HomeUiState> = combine(_localUser, _isLoading, _isResetDialogOpen) { localUser, isLoading, isResetDialogOpen ->
        HomeUiState(
            isLoading = isLoading,
            categories = getCategories(localUser.progress),
            isResetDialogOpen = isResetDialogOpen
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000L),
        initialValue = HomeUiState(isLoading = true)
    )

    fun getCategories(userProgress: HiraganaCategory): List<HomeCategory> {
        val hiraganaCategories = HiraganaCategory.entries
        val categories = mutableListOf<HomeCategory>()

        val categoryOnProgressIndex = userProgress.ordinal
        val unlockedCategories = hiraganaCategories.take(categoryOnProgressIndex + 1)
            .map {
                HomeCategory(
                    id = it.id,
                    title = it.kanaWithNakaguro,
                    isLocked = false
                )
            }
        val testAllLearnedCategory = HomeCategory(
            id = "Test All Learned",
            title = "Test All Learned",
            isLocked = false
        )
        val lockedCategories = hiraganaCategories.drop(categoryOnProgressIndex + 1)
            .map {
                HomeCategory(
                    id = it.id,
                    title = it.kanaWithNakaguro,
                    isLocked = true
                )
            }

        categories.addAll(unlockedCategories)
        categories.add(testAllLearnedCategory)
        categories.addAll(lockedCategories)

        return categories
    }

    fun onDrawerResetButtonClick() {
        _isResetDialogOpen.value = true
    }

    fun onMenuItemClick(drawerState: DrawerState, drawerScope: CoroutineScope) {
        drawerScope.launch {
            when (drawerState.currentValue) {
                DrawerValue.Closed -> drawerState.open()
                DrawerValue.Open -> drawerState.close()
            }
        }
    }

    fun onResetDialogDismiss() {
        _isResetDialogOpen.value = false
    }

    fun onResetDialogConfirm() = viewModelScope.launch {
        userRepository.updateLocalUserProgress(HIMIKASE)
        userRepository.updateLocalUserLearningSetsCount(5)
        userRepository.updateLocalUserIsTestUnlocked(false)
        _isResetDialogOpen.value = false
    }
}