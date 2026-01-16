package com.effatheresoft.mindlesslyhiragana.navigation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.effatheresoft.mindlesslyhiragana.data.model.Hiragana
import com.effatheresoft.mindlesslyhiragana.ui.testquiz.QuizQuestion
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

data class NavigationUiState(
    val loading: Boolean = false,
    val correctCount: Int = -1,
    val incorrectCount: Int = -1,
    val incorrectHiraganaList: List<Hiragana> = emptyList()
)

@HiltViewModel
class NavigationViewModel @Inject constructor(): ViewModel() {
    private val _questionStates = MutableStateFlow<List<QuizQuestion>>(emptyList())
    private val _loading = MutableStateFlow(false)

    private val _correctCount = _questionStates.map { questionStates ->
        questionStates.count { it.answerAttempts.size == 1 }
    }
    private val _incorrectCount = _questionStates.map { questionStates ->
        questionStates.count { it.answerAttempts.size > 1 }
    }
    private val _incorrectHiraganaList = _questionStates.map { questionStates ->
        questionStates.filter { it.answerAttempts.size > 1 }.map { it.question }
    }

    val uiState = combine(_loading, _correctCount, _incorrectCount, _incorrectHiraganaList) { loading, correctCount, incorrectCount, incorrectHiraganaList ->
        NavigationUiState(
            loading = loading,
            correctCount = correctCount,
            incorrectCount = incorrectCount,
            incorrectHiraganaList = incorrectHiraganaList
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = NavigationUiState(loading = true)
    )
}