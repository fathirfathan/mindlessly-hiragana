package com.effatheresoft.mindlesslyhiragana.ui.quiz

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.effatheresoft.mindlesslyhiragana.data.model.Hiragana
import com.effatheresoft.mindlesslyhiragana.data.model.HiraganaCategory
import com.effatheresoft.mindlesslyhiragana.data.model.toHiraganaCategoryOrNull
import com.effatheresoft.mindlesslyhiragana.data.repository.QuizRepository
import com.effatheresoft.mindlesslyhiragana.data.repository.UserRepository
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
    val quizRepository: QuizRepository,
    val userRepository: UserRepository
): ViewModel() {
    @AssistedFactory interface Factory {
        fun create(categoryId: String): QuizViewModel
    }

    init {
        viewModelScope.launch {
            categoryId.toHiraganaCategoryOrNull()?.let {
                quizRepository.generateLearnQuizQuestions(it)
            }
        }
    }

    private val _isLoading = MutableStateFlow(false)
    private val category = categoryId.toHiraganaCategoryOrNull()
    private suspend fun getHighestCategory() = userRepository.observeUser().map { it.highestCategory }.first()
    private val quizQuestionsState = quizRepository.observeQuizQuestions()
    private val quizQuestions get() = quizQuestionsState.value
    private val currentQuestionIndexState = MutableStateFlow(0)
    private val currentQuestionIndex get() = currentQuestionIndexState.value
    private val currentQuestion get() = quizQuestions[currentQuestionIndex]

    private val selectedAnswersState = MutableStateFlow(emptySet<Hiragana>())
    private val mutableUiEvent = MutableSharedFlow<QuizUiEvent>()
    val observableUiEvent: SharedFlow<QuizUiEvent> = mutableUiEvent

    val uiState: StateFlow<QuizUiState> =
        combine(
            _isLoading,
            quizQuestionsState,
            currentQuestionIndexState,
            selectedAnswersState
        ) { isLoading, quizQuestions, currentQuizIndex, selectedAnswers ->

            QuizUiState(
                isLoading = isLoading,
                currentQuiz = quizQuestions.getOrNull(currentQuizIndex)?.question,
                category = category,
                remainingQuestionsCount = quizQuestions.size - currentQuizIndex - 1,
                selectedAnswers = selectedAnswers
            )
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000L),
            initialValue = QuizUiState(isLoading = true)
        )

    fun selectAnswer(answer: Hiragana) = viewModelScope.launch {
        quizRepository.selectAnswer(currentQuestionIndex, answer)
        selectedAnswersState.value += answer

        if (currentQuestion.isAnswered) {
            currentQuestionIndexState.value += 1
            selectedAnswersState.value = emptySet()
        }

        if (quizQuestions.isAllQuestionsAnswered) {
            if (quizQuestions.isAllQuestionsCorrect && category == getHighestCategory()) {
                userRepository.unlockTestAllLearned()
            }

            mutableUiEvent.emit(QuizUiEvent.NavigateToResult)
        }
    }
}