package com.effatheresoft.mindlesslyhiragana.ui.results

import androidx.lifecycle.ViewModel
import com.effatheresoft.mindlesslyhiragana.data.Hiragana
import com.effatheresoft.mindlesslyhiragana.data.HiraganaRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.serialization.Serializable

sealed class ResultsUiState() {
    data object Loading: ResultsUiState()
    data class Success(val quizResults: List<QuizResult>): ResultsUiState()
    data class Error(val exception: Throwable): ResultsUiState()
}

class ResultsViewModel(private val repository: HiraganaRepository): ViewModel() {
    val quizResults = mutableListOf<QuizResult>()

    private val _uiState = MutableStateFlow<ResultsUiState>(ResultsUiState.Loading)
    val uiState = _uiState.asStateFlow()

    fun initializeWithQuizResults(quizResults: List<QuizResult>) {
        this.quizResults.addAll(quizResults)
        _uiState.value = ResultsUiState.Success(this.quizResults)
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
