package com.effatheresoft.mindlesslyhiragana.ui.testresult

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.effatheresoft.mindlesslyhiragana.data.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

data class TestResultUiState(
    val loading: Boolean = false,
    val canContinueLearning: Boolean = false,
)

@HiltViewModel
class TestResultViewModel @Inject constructor(
    userRepository: UserRepository
): ViewModel() {
    private val _loading = MutableStateFlow(false)
    private val _canContinueLearning = userRepository.observeLocalUser().map { !it.isTestUnlocked }

    val uiState = combine(_loading, _canContinueLearning) { loading, canContinueLearning ->
        TestResultUiState(
            loading = loading,
            canContinueLearning = canContinueLearning
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = TestResultUiState(loading = true)
    )
}