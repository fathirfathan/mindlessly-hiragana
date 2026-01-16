package com.effatheresoft.mindlesslyhiragana.data.repository

import com.effatheresoft.mindlesslyhiragana.data.model.Hiragana
import com.effatheresoft.mindlesslyhiragana.data.model.HiraganaCategory
import com.effatheresoft.mindlesslyhiragana.ui.quiz.PossibleAnswer
import com.effatheresoft.mindlesslyhiragana.ui.quiz.Quiz
import com.effatheresoft.mindlesslyhiragana.ui.testquiz.QuizQuestion
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import java.util.Collections
import javax.inject.Inject

open class QuizVolatileDataSource @Inject constructor() {
    private val _quizzes = MutableStateFlow(emptyList<Quiz>())
    private val _quizQuestions = MutableStateFlow(emptyList<QuizQuestion>())

    fun observeQuizzes(): StateFlow<List<Quiz>> = _quizzes
    fun observeQuizQuestions(): StateFlow<List<QuizQuestion>> = _quizQuestions

    fun setQuizzes(quizzes: List<Quiz>) {
        _quizzes.value = quizzes
    }
    fun setQuizQuestions(questions: List<QuizQuestion>) {
        _quizQuestions.value = questions
    }

    fun selectAnswer(index: Int, answer: Hiragana) {
        val updatedQuestions = _quizQuestions.value.toMutableList().apply {
            val targetQuestion = get(index)
            val updatedQuestion = targetQuestion.copy(answerAttempts = targetQuestion.answerAttempts + answer)
            set(index, updatedQuestion)
        }
        _quizQuestions.value = updatedQuestions
    }

    open fun generateQuizzes(
        categoryId: String,
        repeatCount: Int
    ) {
        val hiraganaCategory = HiraganaCategory.entries.first { it.id == categoryId }
        val possibleAnswers = hiraganaCategory.hiraganaList.map {
            PossibleAnswer(
                answer = it,
                isCorrect = false,
                isSelected = false
            )
        }

        val possibleQuizzes = mutableListOf<Quiz>()
        for (hiragana in hiraganaCategory.hiraganaList) {
            possibleQuizzes.add(
                Quiz(
                    question = hiragana,
                    possibleAnswers = possibleAnswers.map {
                        it.copy(isCorrect = it.answer == hiragana)
                    }
                )
            )
        }

        val generatedQuizzes = mutableListOf<Quiz>()
        repeat(repeatCount) {
            val randomizedPossibleQuizzes = possibleQuizzes.shuffled().toMutableList()
            generatedQuizzes.lastOrNull()?.let { lastGeneratedQuiz ->
                if (randomizedPossibleQuizzes.first() == lastGeneratedQuiz) {
                    Collections.swap(randomizedPossibleQuizzes, 0, randomizedPossibleQuizzes.lastIndex)
                }
            }

            generatedQuizzes.addAll(randomizedPossibleQuizzes)
        }
        _quizzes.value = generatedQuizzes
    }

    open fun generateQuizQuestions(categoryId: String) {
        val hiraganaQuestions = HiraganaCategory.progressToCategoryList(categoryId).flatMap { it.hiraganaList }
        val quizQuestions = hiraganaQuestions.map { question -> QuizQuestion(question = question) }
        setQuizQuestions(quizQuestions.shuffled())
    }
}

class RefactoredQuizRepository @Inject constructor(
    private val userRepository: RefactoredUserRepository,
    private val quizVolatileDataSource: QuizVolatileDataSource
) {
    private val observedLearningSetsCount = userRepository.observeLocalUser().map { it.learningSetsCount }
    private val _quizzes = quizVolatileDataSource.observeQuizzes()

    fun observeQuizzes(): StateFlow<List<Quiz>> = _quizzes
    fun observeQuizQuestions() = quizVolatileDataSource.observeQuizQuestions()

    suspend fun generateQuizzes(categoryId: String) {
        quizVolatileDataSource.generateQuizzes(categoryId, observedLearningSetsCount.first())
    }

    suspend fun generateQuizQuestions() {
        val userProgress = userRepository.observeLocalUser().first().progress
        quizVolatileDataSource.generateQuizQuestions(userProgress)
    }

    fun selectAnswer(index: Int, answer: Hiragana) {
        quizVolatileDataSource.selectAnswer(index, answer)
    }

    fun selectQuizAnswer(selectedQuizIndex: Int, selectedAnswer: Hiragana) {
        val quizzes = _quizzes.value.toMutableList()
        if (selectedQuizIndex in quizzes.indices) {
            val currentQuiz = quizzes[selectedQuizIndex]
            quizzes[selectedQuizIndex] = currentQuiz.copy(
                possibleAnswers = currentQuiz.possibleAnswers.map {
                    if (it.answer == selectedAnswer) it.copy(isSelected = true) else it
                }
            )
            quizVolatileDataSource.setQuizzes(quizzes)
        }
    }
}