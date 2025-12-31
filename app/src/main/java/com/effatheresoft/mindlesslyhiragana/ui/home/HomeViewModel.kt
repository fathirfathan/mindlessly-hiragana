package com.effatheresoft.mindlesslyhiragana.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.effatheresoft.mindlesslyhiragana.data.model.HiraganaCategory.HIMIKASE
import com.effatheresoft.mindlesslyhiragana.data.repository.QuizCategory
import com.effatheresoft.mindlesslyhiragana.data.repository.RefactoredQuizRepository
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
    val categories: List<QuizCategory> = emptyList(),
    val selectedCategory: QuizCategory? = null,
    val isResetDialogOpen: Boolean = false
)

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val quizRepository: RefactoredQuizRepository,
    private val userRepository: UserRepository
) : ViewModel() {

    private val _categories = quizRepository.observeQuizCategories()

    private val _localUser = userRepository.observeLocalUser()
    private val _isLoading = MutableStateFlow(false)
    private val _isResetDialogOpen = MutableStateFlow(false)
    private val _selectedCategory = MutableStateFlow<QuizCategory?>(null)

    val uiState: StateFlow<HomeUiState> = combine(
        _localUser,
        _isLoading,
        _isResetDialogOpen,
        _categories,
        _selectedCategory
    ) { localUser, isLoading, isResetDialogOpen, categories, selectedCategory ->

        HomeUiState(
            progress = localUser.progress,
            isLoading = isLoading,
            categories = categories,
            selectedCategory = selectedCategory,
            isResetDialogOpen = isResetDialogOpen
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000L),
        initialValue = HomeUiState(isLoading = true)
    )

    fun selectCategory(category: QuizCategory) {
        val isSuccess = quizRepository.selectCategory(category)
        if (isSuccess) _selectedCategory.value = category
    }

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