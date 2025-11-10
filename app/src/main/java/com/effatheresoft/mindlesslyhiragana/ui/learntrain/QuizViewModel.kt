package com.effatheresoft.mindlesslyhiragana.ui.learntrain

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.effatheresoft.mindlesslyhiragana.data.Hiragana
import com.effatheresoft.mindlesslyhiragana.data.HiraganaRepository
import kotlinx.serialization.Serializable
import kotlin.collections.listOf

data class LearnTrainUiState(
    val appBarTitle: String = "",
    val currentQuestion: String = "",
    val remainingQuestionsCount: String = "",
    val possibleAnswers: List<Hiragana> = listOf()
)

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

class QuizViewModel: ViewModel() {
    private var id = ""
    private var hiraganaList by mutableStateOf(listOf<Hiragana>())
    private var appBarTitle by mutableStateOf("")
    private var questionList by mutableStateOf(listOf<Hiragana>())
    private var currentQuestion by mutableStateOf<Hiragana?>(null)
    private var remainingQuestionsCount by mutableIntStateOf(-1)
    private var possibleAnswers by mutableStateOf(listOf<Hiragana>())
    private val quizResults = mutableListOf<QuizResult>()

    var uiState by mutableStateOf(LearnTrainUiState())
        private set

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
        this.id = id
        hiraganaList = HiraganaRepository.getHiraganaCategoryById(id).hiraganaList
        appBarTitle = hiraganaList.joinToString(" ") { it.romaji.uppercase() }
        questionList = hiraganaList.generateQuestions()
        currentQuestion = questionList.firstOrNull()
        remainingQuestionsCount = questionList.size - 1
        possibleAnswers = hiraganaList

        uiState = uiState.copy(
            appBarTitle = appBarTitle,
            currentQuestion = currentQuestion?.hiragana ?: "",
            remainingQuestionsCount = remainingQuestionsCount.toString(),
            possibleAnswers = possibleAnswers
        )
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

        uiState = uiState.copy(
            currentQuestion = currentQuestion?.hiragana ?: "",
            remainingQuestionsCount = remainingQuestionsCount.toString()
        )
    }
}