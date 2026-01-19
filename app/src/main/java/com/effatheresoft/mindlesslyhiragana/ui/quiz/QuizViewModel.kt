package com.effatheresoft.mindlesslyhiragana.ui.quiz

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.effatheresoft.mindlesslyhiragana.data.model.Hiragana
import com.effatheresoft.mindlesslyhiragana.data.model.HiraganaCategory
import com.effatheresoft.mindlesslyhiragana.data.model.toHiraganaCategoryOrNull
import com.effatheresoft.mindlesslyhiragana.data.repository.RefactoredQuizRepository
import com.effatheresoft.mindlesslyhiragana.data.repository.RefactoredUserRepository
import com.effatheresoft.mindlesslyhiragana.ui.testquiz.isAllQuestionsAnswered
import com.effatheresoft.mindlesslyhiragana.ui.testquiz.isAllQuestionsCorrect
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

data class QuizUiState(
    val isLoading: Boolean = false,
    val currentQuiz: Hiragana? = null,
    val category: HiraganaCategory? = null,
    val remainingQuestionsCount: Int? = null,
    val selectedAnswers: Set<Hiragana> = emptySet()
)

sealed class QuizUiEvent{
    object NavigateToResult: QuizUiEvent()
}

@HiltViewModel(assistedFactory = QuizViewModel.Factory::class)
class QuizViewModel @AssistedInject constructor(
    @Assisted val categoryId: String,
    val quizRepository: RefactoredQuizRepository,
    val userRepository: RefactoredUserRepository
): ViewModel() {
    @AssistedFactory interface Factory {
        fun create(categoryId: String): QuizViewModel
    }

    private val _isLoading = MutableStateFlow(false)
    private val category = categoryId.toHiraganaCategoryOrNull()
    private suspend fun getUserProgress() = userRepository.observeLocalUser().map { it.progress }.first()
    private val _quizQuestions = quizRepository.observeQuizQuestions()
    private val quizQuestions get() = _quizQuestions.value
    private val _currentQuestionIndex = MutableStateFlow(0)
    private val currentQuestionIndex get() = _currentQuestionIndex.value
    private val _selectedAnswers = MutableStateFlow(emptySet<Hiragana>())
    private val currentQuestion get() = quizQuestions[currentQuestionIndex]

    private val _uiEvent = MutableSharedFlow<QuizUiEvent>()
    val uiEvent: SharedFlow<QuizUiEvent> = _uiEvent
    val uiState: StateFlow<QuizUiState> =
        combine(_isLoading, _quizQuestions, _currentQuestionIndex, _selectedAnswers) { isLoading, quizQuestions, currentQuizIndex, selectedAnswers ->
            QuizUiState(
                isLoading = isLoading,
                currentQuiz = quizQuestions.getOrNull(currentQuizIndex)?.question,
                category = categoryId.toHiraganaCategoryOrNull(),
                remainingQuestionsCount = quizQuestions.size - currentQuizIndex - 1,
                selectedAnswers = selectedAnswers
            )
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000L),
            initialValue = QuizUiState(isLoading = true)
        )

    init {
        viewModelScope.launch {
            quizRepository.generateLearnQuizQuestions()
        }
    }

    fun selectAnswer(hiragana: Hiragana) = viewModelScope.launch {
        quizRepository.selectAnswer(currentQuestionIndex, hiragana)
        _selectedAnswers.value += hiragana

        if (currentQuestion.isAnswered) {
            _currentQuestionIndex.value += 1
            _selectedAnswers.value = emptySet()
        }

        if (quizQuestions.isAllQuestionsAnswered) {
            if (quizQuestions.isAllQuestionsCorrect && category == getUserProgress()) {
                userRepository.unlockTestAllLearned()
            }
            _uiEvent.emit(QuizUiEvent.NavigateToResult)
        }
    }
}