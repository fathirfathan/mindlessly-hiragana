package com.effatheresoft.mindlesslyhiragana.data.repository

import com.effatheresoft.mindlesslyhiragana.data.model.Hiragana
import com.effatheresoft.mindlesslyhiragana.data.model.HiraganaCategory
import kotlinx.coroutines.flow.first
import javax.inject.Inject

class QuizRepository @Inject constructor(
    private val userRepository: UserRepository,
    private val quizVolatileDataSource: QuizVolatileDataSource
) {
    fun observeQuizQuestions() = quizVolatileDataSource.observeQuizQuestions()

    suspend fun generateLearnQuizQuestions(currentCategory: HiraganaCategory) {
        val repeatCategoryCount = userRepository.observeUser().first().repeatCategoryCount
        quizVolatileDataSource.generateLearnQuizQuestions(currentCategory, repeatCategoryCount)
    }

    suspend fun generateTestQuizQuestions() {
        val highestCategory = userRepository.observeUser().first().highestCategory
        quizVolatileDataSource.generateTestQuizQuestions(highestCategory)
    }

    fun selectAnswer(index: Int, answer: Hiragana) {
        quizVolatileDataSource.selectAnswer(index, answer)
    }
}