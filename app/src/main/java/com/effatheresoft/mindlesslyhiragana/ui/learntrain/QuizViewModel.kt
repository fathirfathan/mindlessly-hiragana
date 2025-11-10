package com.effatheresoft.mindlesslyhiragana.ui.learntrain

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.effatheresoft.mindlesslyhiragana.data.Hiragana
import com.effatheresoft.mindlesslyhiragana.data.HiraganaCategory
import com.effatheresoft.mindlesslyhiragana.data.HiraganaRepository
import com.effatheresoft.mindlesslyhiragana.util.Result
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.serialization.Serializable
import kotlin.collections.listOf

sealed class QuizUiState {
    data object Loading: QuizUiState()
    data class Success(
        val appBarTitle: String,
        val currentQuestion: String,
        val remainingQuestionsCount: String,
        val possibleAnswers: List<Hiragana>
    ): QuizUiState()
    data class Error(val exception: Throwable): QuizUiState()
}

@Serializable
data class QuizResult(
    val question: Hiragana,
    val answer: Hiragana,
)

fun List<QuizResult>.getCorrectAnswersCount(): Int {
    var count = 0
    for (quizResult in this) {
        if (quizResult.question == quizResult.answer) {
            count++
        }
    }
    return count
}

fun List<QuizResult>.getIncorrectAnswersCount(): Int {
    return size - getCorrectAnswersCount()
}

class QuizViewModel(private val repository: HiraganaRepository): ViewModel() {
    private var appBarTitle = ""
    private var currentQuestion: Hiragana? = null
    private var hiraganaList = listOf<Hiragana>()
    private var id = ""
    private var possibleAnswers = listOf<Hiragana>()
    private var questionList = listOf<Hiragana>()
    private val quizResults = mutableListOf<QuizResult>()
    private var remainingQuestionsCount = -1

    private lateinit var hiraganaCategory: Flow<Result<HiraganaCategory>>

    private val _uiState = MutableStateFlow<QuizUiState>(QuizUiState.Loading)
    val uiState = _uiState.asStateFlow()

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
            remainingQuestionsCount = remainingQuestionsCount.toString(),
            possibleAnswers = possibleAnswers
        )
    }

    fun List<Hiragana>.generateQuestions(): List<Hiragana> {
        if (isEmpty()) {
            return emptyList()
        }

        val multipliedList = mutableListOf<Hiragana>().apply {
            repeat(2) {
                addAll(this@generateQuestions)
            }
        }

        repeat(1000) {
            val questions = multipliedList.shuffled()
            if (questions.zipWithNext().all { it.first != it.second }) {
                return questions
            }
        }

        return multipliedList
    }

    fun initializeWithId(id: String) {
        if (id == this.id) return

        this.id = id
        hiraganaCategory = repository.getHiraganaCategoryById(id)
        hiraganaCategory.onEach {
            when (it) {
                is Result.Success -> {
                    hiraganaList = it.data.hiraganaList
                    questionList = hiraganaList.generateQuestions()

                    appBarTitle = hiraganaList.joinToString(" ") { it.romaji.uppercase() }
                    currentQuestion = questionList.firstOrNull()
                    remainingQuestionsCount = questionList.size - 1
                    possibleAnswers = hiraganaList

                    setUiStateSuccess()
                }
                is Result.Loading -> setUiStateLoading()
                is Result.Error -> setUiStateError(it.exception)
            }
        }.launchIn(viewModelScope)
    }

    fun onAnswerSelected(
        selectedAnswer: Hiragana,
        onNavigateToResults: (quizResults: List<QuizResult>) -> Unit = {}
    ) {
        quizResults.add(QuizResult(currentQuestion!!, selectedAnswer))

        if (remainingQuestionsCount == 0) {
            onNavigateToResults(quizResults)
            return
        }

        currentQuestion = questionList[remainingQuestionsCount]
        remainingQuestionsCount--

        setUiStateSuccess()
    }
}