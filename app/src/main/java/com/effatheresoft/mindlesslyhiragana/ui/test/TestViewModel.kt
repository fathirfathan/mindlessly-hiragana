package com.effatheresoft.mindlesslyhiragana.ui.test

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.effatheresoft.mindlesslyhiragana.data.model.HiraganaCategory
import com.effatheresoft.mindlesslyhiragana.data.repository.RefactoredUserRepository
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
    userRepository: RefactoredUserRepository
): ViewModel() {
    private val _observedUser = userRepository.observeLocalUser()
    private val _loading = MutableStateFlow(false)

    val uiState = combine(_loading, _observedUser) { loading, user ->
        TestUiState(
            loading = loading,
            categoryList = HiraganaCategory.progressToCategoryList(user.progress),
            isTestUnlocked = user.isTestUnlocked
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = TestUiState(loading = true)
    )
}