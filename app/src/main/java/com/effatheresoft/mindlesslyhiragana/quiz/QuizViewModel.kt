package com.effatheresoft.mindlesslyhiragana.quiz

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
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
    val currentQuestion: String = "",
    val remainingQuestionsCount: Int = 0,
    val possibleAnswers: List<String> = listOf()
)

@HiltViewModel
class QuizViewModel @Inject constructor(val userRepository: UserRepository): ViewModel() {

    private val _isLoading = MutableStateFlow(false)
    private val _localUser = userRepository.observeLocalUser()
    private val _questions = MutableStateFlow<List<String>>(emptyList())
    private val _possibleAnswers = MutableStateFlow<List<String>>(emptyList())
    val uiState: StateFlow<QuizUiState> =
        combine(_isLoading, _questions, _possibleAnswers) { isLoading, questions, possibleAnswers ->
            QuizUiState(
                isLoading = isLoading,
                currentQuestion = questions.firstOrNull() ?: "",
                remainingQuestionsCount = questions.size - 1,
                possibleAnswers = possibleAnswers
            )
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000L),
            initialValue = QuizUiState(isLoading = true)
        )

    fun generateQuiz(categoryId: String) {
        viewModelScope.launch {
            _isLoading.value = true
            _questions.value = generateQuiz(categoryId, _localUser.first().learningSetsCount)
            _possibleAnswers.value = HiraganaCategory.entries.first { it.id == categoryId }.hiraganaList
                .map { it.name }
            _isLoading.value = false
        }
    }

    private fun generateQuiz(categoryId: String, multiples: Int): List<String> {
        val possibleQuestions = HiraganaCategory.entries.first { it.id == categoryId }.hiraganaList
            .map { it.kana }

        val questions = mutableListOf<String>()
        repeat(multiples) {
            questions.addAll(possibleQuestions)
        }

        return questions
    }
}