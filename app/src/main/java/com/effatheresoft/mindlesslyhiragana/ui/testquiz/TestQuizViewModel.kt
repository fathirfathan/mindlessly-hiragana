package com.effatheresoft.mindlesslyhiragana.ui.testquiz

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.effatheresoft.mindlesslyhiragana.data.model.Hiragana
import com.effatheresoft.mindlesslyhiragana.data.repository.RefactoredQuizRepository
import com.effatheresoft.mindlesslyhiragana.data.repository.RefactoredUserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

data class QuizQuestion(
    val question: Hiragana,
    val answerAttempts: List<Hiragana> = emptyList()
) {
    val isCorrect: Boolean get() = answerAttempts.size == 1 && answerAttempts.first() == question
    val isAnswered: Boolean get() = answerAttempts.lastOrNull()?.let { it == question } ?: false
}
val List<QuizQuestion>.correctCounts: Int get() = count { it.isCorrect }
val List<QuizQuestion>.incorrectCounts: Int get() = count { !it.isCorrect }
val List<QuizQuestion>.incorrectQuestions: List<QuizQuestion> get() = filter { !it.isCorrect }
val List<QuizQuestion>.isAllQuestionsAnswered: Boolean get() = lastOrNull()?.isAnswered ?: false
val List<QuizQuestion>.isAllQuestionsCorrect: Boolean get() = all { it.isCorrect }

data class TestQuizUiState(
    val loading: Boolean = false,
    val currentQuestion: Hiragana? = null,
    val remainingQuestionsCount: Int = -1,
)

sealed class TestQuizUiEvent() {
    object NavigateToTestResults: TestQuizUiEvent()
}

@HiltViewModel
class TestQuizViewModel @Inject constructor(
    val userRepository: RefactoredUserRepository,
    val quizRepository: RefactoredQuizRepository
): ViewModel() {
    private val _loading = MutableStateFlow(false)
    private val _quizQuestions = quizRepository.observeQuizQuestions()
    private val quizQuestions get() = _quizQuestions.value
    private val _currentQuestionIndex = MutableStateFlow(0)
    private val currentQuestionIndex get() = _currentQuestionIndex.value

    init {
        viewModelScope.launch { quizRepository.generateQuizQuestions() }
    }

    val uiState = combine(_loading, _quizQuestions, _currentQuestionIndex) { loading, questionStates, currentQuestionIndex ->
        TestQuizUiState(
            loading = loading,
            currentQuestion = questionStates.getOrNull(currentQuestionIndex)?.question,
            remainingQuestionsCount = questionStates.size - currentQuestionIndex - 1
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = TestQuizUiState(loading = true)
    )

    private val _uiEvent = MutableSharedFlow<TestQuizUiEvent>()
    val uiEvent: SharedFlow<TestQuizUiEvent> = _uiEvent

    private val currentQuestion get() = quizQuestions[currentQuestionIndex]

    fun selectAnswer(hiragana: Hiragana) = viewModelScope.launch {
        quizRepository.selectAnswer(currentQuestionIndex, hiragana)

        if (currentQuestion.isAnswered) { _currentQuestionIndex.value += 1 }
        if (quizQuestions.isAllQuestionsAnswered) {
            if (quizQuestions.isAllQuestionsCorrect) {
                userRepository.continueLocalUserProgress()
                userRepository.lockTestAllLearned()
            }
            _uiEvent.emit(TestQuizUiEvent.NavigateToTestResults)
        }
    }
}