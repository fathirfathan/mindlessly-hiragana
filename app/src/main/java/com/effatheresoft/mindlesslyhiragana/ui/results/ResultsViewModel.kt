package com.effatheresoft.mindlesslyhiragana.ui.results

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.effatheresoft.mindlesslyhiragana.data.Hiragana
import com.effatheresoft.mindlesslyhiragana.data.UserRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.serialization.Serializable

sealed class ResultsUiState() {
    data object Loading: ResultsUiState()
    data class Success(val quizResults: List<QuizResult>): ResultsUiState()
    data class Error(val exception: Throwable): ResultsUiState()
}

class ResultsViewModel(
    private val quizResults: List<QuizResult>,
    private val userRepository: UserRepository
): ViewModel() {
    private val _uiState = MutableStateFlow<ResultsUiState>(ResultsUiState.Loading)
    val uiState = _uiState.asStateFlow()

    init {
        if (quizResults.incorrectAnswersCount == 0) {
            userRepository.getDefaultUser().onEach {
                when(it) {
                    is com.effatheresoft.mindlesslyhiragana.util.Result.Success -> {
                        it.data?.run{
                            val newHighestCategoryId = highestCategoryId.toInt().run {
                                if (this == 11) 11 else this + 1
                            }.toString()

                            userRepository.updateUser(
                                copy(highestCategoryId = newHighestCategoryId)
                            ).onEach { result ->
                                if (result is com.effatheresoft.mindlesslyhiragana.util.Result.Success)
                                    _uiState.value = ResultsUiState.Success(quizResults)
                            }.launchIn(viewModelScope)
                        }
                    }
                    is com.effatheresoft.mindlesslyhiragana.util.Result.Error -> {}
                    is com.effatheresoft.mindlesslyhiragana.util.Result.Loading -> {}
                }
            }.launchIn(viewModelScope)
        } else {
            _uiState.value = ResultsUiState.Success(quizResults)
        }
    }
}


@Serializable
data class QuizResult(
    val question: Hiragana,
    val attemptedAnswers: Map<Hiragana, Boolean>
)

val QuizResult.isCorrect: Boolean
    get() {
        val selectedAnswer = attemptedAnswers.filterValues { it }.keys.first()
        return attemptedAnswers.values.count { it } == 1 && selectedAnswer == question
    }

val QuizResult.incorrectAttempts: List<Hiragana>
    get() {
        return attemptedAnswers.filterValues { it }.filterKeys { it != question }.keys.toList()
    }

val List<QuizResult>.correctAnswersCount: Int
    get() {
        var count = 0
        for (quizResult in this) {
            if (quizResult.isCorrect) {
                count++
            }
        }
        return count
    }

val List<QuizResult>.incorrectAnswersCount: Int
    get() {
        return size - correctAnswersCount
    }
