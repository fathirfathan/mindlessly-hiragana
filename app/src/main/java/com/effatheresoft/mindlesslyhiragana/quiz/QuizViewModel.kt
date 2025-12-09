package com.effatheresoft.mindlesslyhiragana.quiz

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.effatheresoft.mindlesslyhiragana.data.Hiragana
import com.effatheresoft.mindlesslyhiragana.data.repository.QuizRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

data class QuizUiState(
    val isLoading: Boolean = false,
    val currentQuiz: Quiz? = null,
    val remainingQuestionsCount: Int = -1,
)

@HiltViewModel
class QuizViewModel @Inject constructor(val quizRepository: QuizRepository): ViewModel() {

    private val _isLoading = MutableStateFlow(false)
    private val _quizzes = quizRepository.observeQuizzes()
    private val _currentQuestionIndex = MutableStateFlow(0)

    val uiState: StateFlow<QuizUiState> =
        combine(_isLoading, _quizzes, _currentQuestionIndex) { isLoading, quizzes, currentQuestionIndex ->
            QuizUiState(
                isLoading = isLoading,
                currentQuiz = quizzes.getOrNull(currentQuestionIndex),
                remainingQuestionsCount = quizzes.size - currentQuestionIndex - 1
            )
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000L),
            initialValue = QuizUiState(isLoading = true)
        )

    fun selectCurrentQuizAnswer(answer: Hiragana) =
        quizRepository.selectQuizAnswer(_currentQuestionIndex.value, answer)

    fun generateQuizzes(categoryId: String) = viewModelScope.launch {
        quizRepository.generateQuizzes(categoryId)
    }
}