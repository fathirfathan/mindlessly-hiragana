package com.effatheresoft.mindlesslyhiragana.ui.testquiz

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.effatheresoft.mindlesslyhiragana.data.model.Hiragana
import com.effatheresoft.mindlesslyhiragana.data.repository.QuizRepository
import com.effatheresoft.mindlesslyhiragana.data.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

data class TestQuizUiState(
    val loading: Boolean = false,
    val currentQuestion: Hiragana? = null,
    val remainingQuestionsCount: Int? = null,
    val selectedAnswers: Set<Hiragana> = emptySet()
)

sealed class TestQuizUiEvent() {
    object NavigateToTestResults: TestQuizUiEvent()
}

@HiltViewModel
class TestQuizViewModel @Inject constructor(
    val userRepository: UserRepository,
    val quizRepository: QuizRepository
): ViewModel() {
    init {
        viewModelScope.launch { quizRepository.generateTestQuizQuestions() }
    }

    private val _loading = MutableStateFlow(false)
    private val observableQuizQuestions = quizRepository.observeQuizQuestions()
    private val quizQuestions get() = observableQuizQuestions.value
    private val currentQuestionIndexState = MutableStateFlow(0)
    private val currentQuestionIndex get() = currentQuestionIndexState.value
    private val currentQuestion get() = quizQuestions[currentQuestionIndex]
    private val selectedAnswersState = MutableStateFlow(emptySet<Hiragana>())

    private val mutableUiEvent = MutableSharedFlow<TestQuizUiEvent>()
    val observableUiEvent: SharedFlow<TestQuizUiEvent> = mutableUiEvent

    val uiState = combine(
        _loading,
        observableQuizQuestions,
        currentQuestionIndexState,
        selectedAnswersState
    ) { loading, quizQuestions, currentQuestionIndex, selectedAnswers ->

        TestQuizUiState(
            loading = loading,
            currentQuestion = quizQuestions.getOrNull(currentQuestionIndex)?.question,
            remainingQuestionsCount = quizQuestions.size - currentQuestionIndex - 1,
            selectedAnswers = selectedAnswers
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = TestQuizUiState(loading = true)
    )

    fun selectAnswer(answer: Hiragana) = viewModelScope.launch {
        quizRepository.selectAnswer(currentQuestionIndex, answer)
        selectedAnswersState.value += answer

        if (currentQuestion.isAnswered) {
            currentQuestionIndexState.value += 1
            selectedAnswersState.value = emptySet()
        }

        if (quizQuestions.isAllQuestionsAnswered) {
            if (quizQuestions.isAllQuestionsCorrect) {
                userRepository.advanceHighestCategory()
                userRepository.lockTestAllLearned()
            }
            mutableUiEvent.emit(TestQuizUiEvent.NavigateToTestResults)
        }
    }
}