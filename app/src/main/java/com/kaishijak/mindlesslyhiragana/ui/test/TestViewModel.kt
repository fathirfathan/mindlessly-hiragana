package com.kaishijak.mindlesslyhiragana.ui.test

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kaishijak.mindlesslyhiragana.data.model.HiraganaCategory
import com.kaishijak.mindlesslyhiragana.data.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

data class TestUiState(
    val loading: Boolean = false,
    val categoryList: List<HiraganaCategory> = emptyList(),
    val isTestUnlocked: Boolean = false
)

@HiltViewModel
class TestViewModel @Inject constructor(
    userRepository: UserRepository
): ViewModel() {
    private val observableUser = userRepository.observeUser()
    private val _loading = MutableStateFlow(false)

    val uiState = combine(_loading, observableUser) { loading, user ->
        TestUiState(
            loading = loading,
            categoryList = user.highestCategory.complementedHiraganaCategory,
            isTestUnlocked = user.isTestUnlocked
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = TestUiState(loading = true)
    )
}