package com.effatheresoft.mindlesslyhiragana.ui.quiz

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.effatheresoft.mindlesslyhiragana.data.model.Hiragana
import com.effatheresoft.mindlesslyhiragana.data.repository.RefactoredQuizRepository
import com.effatheresoft.mindlesslyhiragana.data.repository.RefactoredUserRepository
import com.effatheresoft.mindlesslyhiragana.ui.result.isCorrect
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
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

data class QuizUiState(
    val isLoading: Boolean = false,
    val currentQuiz: Quiz? = null,
    val remainingQuestionsCount: Int = -1
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

    init {
        viewModelScope.launch {
            quizRepository.generateQuizzes(categoryId)
        }
    }

    private val _isLoading = MutableStateFlow(false)
    private val _quizzes = quizRepository.observeQuizzes()
    private val _currentQuizIndex = MutableStateFlow(0)

    val uiState: StateFlow<QuizUiState> =
        combine(_isLoading, _quizzes, _currentQuizIndex) { isLoading, quizzes, currentQuizIndex ->
            QuizUiState(
                isLoading = isLoading,
                currentQuiz = quizzes.getOrNull(currentQuizIndex),
                remainingQuestionsCount = quizzes.size - currentQuizIndex - 1
            )
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000L),
            initialValue = QuizUiState(isLoading = true)
        )

    private val _uiEvent = MutableSharedFlow<QuizUiEvent>()
    val uiEvent: SharedFlow<QuizUiEvent> = _uiEvent

    fun selectCurrentQuizAnswer(answer: Hiragana) = viewModelScope.launch {
        quizRepository.selectQuizAnswer(_currentQuizIndex.value, answer)

        val currentQuiz = _quizzes.first()[_currentQuizIndex.value]
        val selectedAnswer = currentQuiz.possibleAnswers.first { it.answer == answer }
        if (selectedAnswer.isCorrect) {
            if (_currentQuizIndex.value == _quizzes.first().size - 1) {
                val localUser = userRepository.observeLocalUser().first()
                if (localUser.progress == categoryId) {
                    val isAllCorrect = _quizzes.first().firstOrNull { !it.isCorrect }.let { it == null }
                    if (isAllCorrect) userRepository.updateLocalUserIsTestUnlocked(true)
                }
                _uiEvent.emit(QuizUiEvent.NavigateToResult)
            } else {
                _currentQuizIndex.value += 1
            }
        }
    }
}