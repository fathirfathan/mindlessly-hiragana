package com.effatheresoft.mindlesslyhiragana.data.repository

import com.effatheresoft.mindlesslyhiragana.data.model.Hiragana
import kotlinx.coroutines.flow.first
import javax.inject.Inject

class QuizRepository @Inject constructor(
    private val userRepository: UserRepository,
    private val quizVolatileDataSource: QuizVolatileDataSource
) {
    fun observeQuizQuestions() = quizVolatileDataSource.observeQuizQuestions()

    suspend fun generateLearnQuizQuestions() {
        val highestCategory = userRepository.observeUser().first().highestCategory
        val repeatCategoryCount = userRepository.observeUser().first().repeatCategoryCount
        quizVolatileDataSource.generateLearnQuizQuestions(highestCategory, repeatCategoryCount)
    }

    suspend fun generateTestQuizQuestions() {
        val highestCategory = userRepository.observeUser().first().highestCategory
        quizVolatileDataSource.generateTestQuizQuestions(highestCategory)
    }

    fun selectAnswer(index: Int, answer: Hiragana) {
        quizVolatileDataSource.selectAnswer(index, answer)
    }
}