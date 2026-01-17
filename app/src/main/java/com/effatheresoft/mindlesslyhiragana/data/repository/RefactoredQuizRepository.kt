package com.effatheresoft.mindlesslyhiragana.data.repository

import com.effatheresoft.mindlesslyhiragana.data.model.Hiragana
import com.effatheresoft.mindlesslyhiragana.ui.quiz.Quiz
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import javax.inject.Inject

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