package com.effatheresoft.mindlesslyhiragana.quiz

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.effatheresoft.mindlesslyhiragana.data.Hiragana
import com.effatheresoft.mindlesslyhiragana.data.HiraganaCategory
import com.effatheresoft.mindlesslyhiragana.data.UserRepository
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
    val remainingQuestionsCount: Int = 0,
)

@HiltViewModel
class QuizViewModel @Inject constructor(val userRepository: UserRepository): ViewModel() {

    private val _isLoading = MutableStateFlow(false)
    private val _localUser = userRepository.observeLocalUser()
    private val _quizzes = MutableStateFlow<List<Quiz>>(emptyList())
    private val _currentQuestionIndex = MutableStateFlow(0)
    private val _currentQuiz = combine(_quizzes, _currentQuestionIndex) { quizzes, currentQuestionIndex ->
        quizzes.getOrNull(currentQuestionIndex)
    }
    val uiState: StateFlow<QuizUiState> =
        combine(_isLoading, _currentQuiz, _quizzes, _currentQuestionIndex) { isLoading, currentQuiz, quizzes, currentQuestionIndex ->
            QuizUiState(
                isLoading = isLoading,
                currentQuiz = currentQuiz,
                remainingQuestionsCount = quizzes.size - currentQuestionIndex - 1
            )
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000L),
            initialValue = QuizUiState(isLoading = true)
        )

    fun generateQuizzes(categoryId: String) {
        viewModelScope.launch {
            _isLoading.value = true
            val hiraganaCategory = HiraganaCategory.entries.first { it.id == categoryId }
            val possibleAnswers = hiraganaCategory.hiraganaList.map { hiragana ->
                PossibleAnswer(
                    answer = hiragana,
                    isCorrect = false,
                    isSelected = false
                )
            }
            val possibleQuizzes = hiraganaCategory.hiraganaList.map { hiragana ->
                Quiz(
                    question = hiragana,
                    possibleAnswers = possibleAnswers.map { answer ->
                        answer.copy(isCorrect = answer.answer == hiragana)
                    }
                )
            }

            val quizzes = mutableListOf<Quiz>()
            repeat(_localUser.first().learningSetsCount) {
                quizzes.addAll(possibleQuizzes)
            }
            _quizzes.value = quizzes

            _isLoading.value = false
        }
    }

    fun selectCurrentQuizAnswer(answer: Hiragana) {
        viewModelScope.launch {
            _quizzes.value = _quizzes.value.mapIndexed { index, quiz ->
                if (index == _currentQuestionIndex.value) {
                    selectQuizAnswer(quiz, answer)
                } else {
                    quiz
                }
            }
        }
    }

    private fun selectQuizAnswer(quiz: Quiz, selectedAnswer: Hiragana): Quiz {
        return quiz.copy(
            possibleAnswers = selectPossibleAnswer(quiz.possibleAnswers, selectedAnswer)
        )
    }

    private fun selectPossibleAnswer(possibleAnswers: List<PossibleAnswer>, selectedAnswer: Hiragana): List<PossibleAnswer> {
        return possibleAnswers.map {
            if (it.answer == selectedAnswer) {
                it.copy(isSelected = true)
            } else {
                it
            }
        }
    }
}