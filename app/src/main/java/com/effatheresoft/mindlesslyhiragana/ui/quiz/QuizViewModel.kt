package com.effatheresoft.mindlesslyhiragana.ui.quiz

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.effatheresoft.mindlesslyhiragana.data.model.Hiragana
import com.effatheresoft.mindlesslyhiragana.data.repository.QuizRepository
import com.effatheresoft.mindlesslyhiragana.data.repository.UserRepository
import com.effatheresoft.mindlesslyhiragana.ui.result.isCorrect
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

data class QuizUiState(
    val isLoading: Boolean = false,
    val currentQuiz: Quiz? = null,
    val remainingQuestionsCount: Int = -1,
    val isCompleted: Boolean = false
)

@HiltViewModel
class QuizViewModel @Inject constructor(
    val quizRepository: QuizRepository,
    val userRepository: UserRepository
): ViewModel() {

    private val _isLoading = MutableStateFlow(false)
    private val _categoryId = MutableStateFlow("")
    private val _quizzes = quizRepository.observeQuizzes()
    private val _currentQuizIndex = MutableStateFlow(0)
    private val _isCompleted = MutableStateFlow(false)

    val uiState: StateFlow<QuizUiState> =
        combine(_isLoading, _quizzes, _currentQuizIndex, _isCompleted) { isLoading, quizzes, currentQuizIndex, isCompleted ->
            QuizUiState(
                isLoading = isLoading,
                currentQuiz = quizzes.getOrNull(currentQuizIndex),
                remainingQuestionsCount = quizzes.size - currentQuizIndex - 1,
                isCompleted = isCompleted
            )
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000L),
            initialValue = QuizUiState(isLoading = true)
        )

    fun selectCurrentQuizAnswer(answer: Hiragana) = viewModelScope.launch {
        quizRepository.selectQuizAnswer(_currentQuizIndex.value, answer)

        val currentQuiz = _quizzes.first()[_currentQuizIndex.value]
        val selectedAnswer = currentQuiz.possibleAnswers.first { it.answer == answer }
        if (selectedAnswer.isCorrect) {
            if (_currentQuizIndex.value == _quizzes.first().size - 1) {
                val localUser = userRepository.observeLocalUser().first()
                if (localUser.progress == _categoryId.value) {
                    val isAllCorrect = _quizzes.first().firstOrNull { !it.isCorrect }.let { it == null }
                    if (isAllCorrect) userRepository.updateLocalUserIsTestUnlocked(true)
                }
                _isCompleted.value = true
            } else {
                _currentQuizIndex.value += 1
            }
        }
    }

    fun generateQuizzes(categoryId: String) = viewModelScope.launch {
        _categoryId.value = categoryId
        quizRepository.generateQuizzes(categoryId)
    }
}