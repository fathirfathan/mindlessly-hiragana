package com.effatheresoft.mindlesslyhiragana.ui.result

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.effatheresoft.mindlesslyhiragana.data.model.Hiragana
import com.effatheresoft.mindlesslyhiragana.data.repository.RefactoredQuizRepository
import com.effatheresoft.mindlesslyhiragana.data.repository.RefactoredUserRepository
import com.effatheresoft.mindlesslyhiragana.ui.testquiz.correctCounts
import com.effatheresoft.mindlesslyhiragana.ui.testquiz.incorrectCounts
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

data class ResultUiState(
    val isLoading: Boolean = false,
    val correctCounts: Int? = null,
    val incorrectCounts: Int? = null,
    val individualIncorrectCounts: List<Pair<Hiragana, Int>> = emptyList(),
    val isTestUnlocked: Boolean = false
)

@HiltViewModel
class ResultViewModel @Inject constructor(
    quizRepository: RefactoredQuizRepository,
    userRepository: RefactoredUserRepository
) : ViewModel() {

    private val _isLoading = MutableStateFlow(false)
    private val _quizQuestions = quizRepository.observeQuizQuestions()
    private val _observedIsTestUnlocked = userRepository.observeLocalUser().map { it.isTestUnlocked }

    val uiState = combine(_isLoading, _quizQuestions, _observedIsTestUnlocked) { isLoading, quizQuestions, isTestUnlocked ->
        val individualIncorrectCounts = quizQuestions.groupBy { quiz -> quiz.question }.map { (hiragana, groupedQuizzes) ->
            hiragana to groupedQuizzes.count { quiz -> !quiz.isCorrect }
        }

        ResultUiState(
            isLoading = isLoading,
            correctCounts = quizQuestions.correctCounts,
            incorrectCounts = quizQuestions.incorrectCounts,
            individualIncorrectCounts = individualIncorrectCounts,
            isTestUnlocked = isTestUnlocked
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = ResultUiState(isLoading = false)
    )
}