package com.effatheresoft.mindlesslyhiragana.ui.results

import androidx.lifecycle.ViewModel
import com.effatheresoft.mindlesslyhiragana.data.HiraganaRepository
import com.effatheresoft.mindlesslyhiragana.ui.learntrain.QuizResult
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

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
