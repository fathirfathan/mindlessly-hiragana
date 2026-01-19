package com.effatheresoft.mindlesslyhiragana.data.repository

import com.effatheresoft.mindlesslyhiragana.data.model.Hiragana
import kotlinx.coroutines.flow.first
import javax.inject.Inject

class RefactoredQuizRepository @Inject constructor(
    private val userRepository: RefactoredUserRepository,
    private val quizVolatileDataSource: QuizVolatileDataSource
) {
    fun observeQuizQuestions() = quizVolatileDataSource.observeQuizQuestions()

    suspend fun generateLearnQuizQuestions() {
        val userProgress = userRepository.observeLocalUser().first().progress
        val learningSetsCount = userRepository.observeLocalUser().first().learningSetsCount
        quizVolatileDataSource.generateLearnQuizQuestions(userProgress, learningSetsCount)
    }

    suspend fun generateTestQuizQuestions() {
        val userProgress = userRepository.observeLocalUser().first().progress
        quizVolatileDataSource.generateTestQuizQuestions(userProgress)
    }

    fun selectAnswer(index: Int, answer: Hiragana) {
        quizVolatileDataSource.selectAnswer(index, answer)
    }
}