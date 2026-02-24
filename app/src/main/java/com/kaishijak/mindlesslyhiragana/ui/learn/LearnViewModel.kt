package com.kaishijak.mindlesslyhiragana.ui.learn

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kaishijak.mindlesslyhiragana.data.model.HiraganaCategory
import com.kaishijak.mindlesslyhiragana.data.repository.UserRepository
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

data class LearnUiState(
    val isLoading: Boolean = false,
    val category: HiraganaCategory? = null,
    val repeatCategoryCount: Int? = null
)

@HiltViewModel(assistedFactory = LearnViewModel.Factory::class)
class LearnViewModel @AssistedInject constructor(
    @Assisted val categoryId: String,
    val userRepository: UserRepository
) : ViewModel() {
    @AssistedFactory interface Factory {
        fun create(categoryId: String): LearnViewModel
    }

    private val observableUser = userRepository.observeUser()
    private val _isLoading = MutableStateFlow(false)

    val uiState: StateFlow<LearnUiState> = combine(observableUser, _isLoading) {
        user, isLoading ->

        LearnUiState(
            isLoading = isLoading,
            category = HiraganaCategory.entries.first { it.id == categoryId },
            repeatCategoryCount = user.repeatCategoryCount
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000L),
        initialValue = LearnUiState(isLoading = true)
    )

    fun updateRepeatCategoryCount(count: Int) = viewModelScope.launch {
        userRepository.updateRepeatCategoryCount(count)
    }
}