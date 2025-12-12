package com.effatheresoft.mindlesslyhiragana.data.repository

import com.effatheresoft.mindlesslyhiragana.data.Hiragana
import com.effatheresoft.mindlesslyhiragana.ui.quiz.Quiz
import kotlinx.coroutines.flow.Flow

interface QuizRepository {
    fun observeQuizzes(): Flow<List<Quiz>>

    fun selectQuizAnswer(selectedQuizIndex: Int, selectedAnswer: Hiragana)

    suspend fun generateQuizzes(categoryId: String)
}