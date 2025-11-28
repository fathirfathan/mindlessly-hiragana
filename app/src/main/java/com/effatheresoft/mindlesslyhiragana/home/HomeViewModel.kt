package com.effatheresoft.mindlesslyhiragana.home

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
import javax.inject.Inject

data class HomeUiState(
    val progress: String = "himikase",
    val isLoading: Boolean = false,
    val unlockedCategories: List<HiraganaCategory> = emptyList(),
    val lockedCategories: List<HiraganaCategory> = emptyList()
)

@HiltViewModel
class HomeViewModel @Inject constructor(val userRepository: UserRepository) : ViewModel() {

    private val hiraganaCategories = Hiragana.getCategories()

    private val _localUser = userRepository.getLocalUser()
    private val _isLoading = MutableStateFlow(false)

    val uiState: StateFlow<HomeUiState> = combine(_localUser, _isLoading) { localUser, isLoading ->
        val currentProgressCategoryIndex = hiraganaCategories.indexOfFirst { it.id == localUser.progress }

        HomeUiState(
            progress = localUser.progress,
            isLoading = isLoading,
            unlockedCategories = hiraganaCategories.take(currentProgressCategoryIndex + 1),
            lockedCategories = hiraganaCategories.drop(currentProgressCategoryIndex + 1)
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000L),
        initialValue = HomeUiState(isLoading = true)
    )
}