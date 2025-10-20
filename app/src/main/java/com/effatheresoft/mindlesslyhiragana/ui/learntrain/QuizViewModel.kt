package com.effatheresoft.mindlesslyhiragana.ui.learntrain

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.effatheresoft.mindlesslyhiragana.data.Hiragana
import com.effatheresoft.mindlesslyhiragana.data.UserRepository
import com.effatheresoft.mindlesslyhiragana.data.generateQuestions
import com.effatheresoft.mindlesslyhiragana.data.getCategoryById
import com.effatheresoft.mindlesslyhiragana.data.getLearnedHiraganaUpToId
import com.effatheresoft.mindlesslyhiragana.ui.results.QuizResult
import com.effatheresoft.mindlesslyhiragana.util.Result
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlin.collections.listOf

class QuizViewModel(
    private val categoryId: String,
    private val learningSetsCount: Int,
    private val userRepository: UserRepository
): ViewModel() {
    private var appBarTitle = ""
    private var currentQuestion: Hiragana? = null
    private var hiraganaList = listOf<Hiragana>()
    private var possibleAnswers = listOf<Hiragana>()
    private var questionList = listOf<Hiragana>()
    private val quizResults = mutableListOf<QuizResult>()
    private var remainingQuestionsCount = -1
    private var selectedAnswersHistory = mapOf<Hiragana, Boolean>()

    private val _uiState = MutableStateFlow<QuizUiState>(QuizUiState.Loading)
    val uiState = _uiState.asStateFlow()

    init {
        if (categoryId == "12") {
            viewModelScope.launch {
                userRepository.getDefaultUser().collect { result ->
                    when (result) {
                        is Result.Error -> setUiStateError(result.exception)
                        is Result.Success -> {
                            result.data?.apply {
                                hiraganaList = Hiragana.categories.getLearnedHiraganaUpToId(highestCategoryId)
                                questionList = hiraganaList.shuffled()
                                currentQuestion = questionList.firstOrNull()
                                remainingQuestionsCount = questionList.size - 1

                                appBarTitle = "Test All Learned"
                                possibleAnswers = hiraganaList.sortedBy { it.romaji }
                                selectedAnswersHistory = hiraganaList.associateWith { false }

                                setUiStateSuccess()
                            }
                        }
                        is Result.Loading -> setUiStateLoading()
                    }
                }
            }
        } else {
            hiraganaList = Hiragana.categories.getCategoryById(categoryId)?.apply {
                questionList = generateQuestions(learningSetsCount)
                currentQuestion = questionList.firstOrNull()
                remainingQuestionsCount = questionList.size - 1

                appBarTitle = hiraganaList.joinToString(" ") { it.romaji.uppercase() }
                possibleAnswers = hiraganaList.sortedBy { it.romaji }
                selectedAnswersHistory = hiraganaList.associateWith { false }

                setUiStateSuccess()
            }?.hiraganaList ?: listOf()
        }
    }

    fun setUiStateError(exception: Throwable) {
        _uiState.value = QuizUiState.Error(exception)
    }

    fun setUiStateLoading() {
        _uiState.value = QuizUiState.Loading
    }

    fun setUiStateSuccess() {
        _uiState.value = QuizUiState.Success(
            appBarTitle = appBarTitle,
            currentQuestion = currentQuestion?.hiragana ?: "",
            remainingQuestionsCount = remainingQuestionsCount,
            possibleAnswers = possibleAnswers,
            selectedAnswersHistory = selectedAnswersHistory
        )
    }

    fun onAnswerSelected(
        selectedAnswer: Hiragana,
        onNavigateToResults: (quizResults: List<QuizResult>) -> Unit = {}
    ) {
        selectedAnswersHistory = selectedAnswersHistory + (selectedAnswer to true)
        if (selectedAnswer != currentQuestion) {
            setUiStateSuccess()
            return
        }
        quizResults.add(QuizResult(
            question = currentQuestion!!,
            attemptedAnswers = selectedAnswersHistory
        ))

        if (remainingQuestionsCount == 0) {
            onNavigateToResults(quizResults)
            return
        }

        currentQuestion = questionList[remainingQuestionsCount]
        remainingQuestionsCount--
        selectedAnswersHistory = hiraganaList.associateWith { false }

        setUiStateSuccess()
    }
}

sealed class QuizUiState {
    data object Loading: QuizUiState()
    data class Success(
        val appBarTitle: String,
        val currentQuestion: String,
        val remainingQuestionsCount: Int,
        val possibleAnswers: List<Hiragana>,
        val selectedAnswersHistory: Map<Hiragana, Boolean>
    ): QuizUiState()
    data class Error(val exception: Throwable): QuizUiState()
}