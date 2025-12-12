package com.effatheresoft.mindlesslyhiragana.ui.result

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.effatheresoft.mindlesslyhiragana.data.Hiragana
import com.effatheresoft.mindlesslyhiragana.data.repository.QuizRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn

data class ResultUiState(
    val isLoading: Boolean = false,
    val correctCounts: Int = -1,
    val incorrectCounts: Int = -1,
    val individualIncorrectCounts: List<Pair<Hiragana, Int>> = emptyList()
)

@HiltViewModel
class ResultViewModel @Inject constructor(
    private val quizRepository: QuizRepository
) : ViewModel() {

    private val _isLoading = MutableStateFlow(false)
    private val _quizzes = quizRepository.observeQuizzes()

    val uiState = combine(_isLoading, _quizzes) { isLoading, quizzes ->
        val individualIncorrectCounts = quizzes.groupBy { quiz -> quiz.question }.map { (hiragana, groupedQuizzes) ->
            hiragana to groupedQuizzes.count { quiz -> !quiz.isCorrect }
        }

        ResultUiState(
            isLoading = isLoading,
            correctCounts = quizzes.correctCounts,
            incorrectCounts = quizzes.incorrectCounts,
            individualIncorrectCounts = individualIncorrectCounts
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = ResultUiState(isLoading = false)
    )
}