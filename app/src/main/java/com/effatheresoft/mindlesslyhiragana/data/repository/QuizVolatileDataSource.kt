package com.effatheresoft.mindlesslyhiragana.data.repository

import com.effatheresoft.mindlesslyhiragana.data.model.Hiragana
import com.effatheresoft.mindlesslyhiragana.data.model.HiraganaCategory
import com.effatheresoft.mindlesslyhiragana.ui.testquiz.QuizQuestion
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import java.util.Collections
import javax.inject.Inject

open class QuizVolatileDataSource @Inject constructor() {
    private val _quizQuestions = MutableStateFlow(emptyList<QuizQuestion>())

    fun observeQuizQuestions(): StateFlow<List<QuizQuestion>> = _quizQuestions

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

    open fun generateLearnQuizQuestions(category: HiraganaCategory, learningSetsCount: Int) {
        val generatedQuestions = mutableListOf<QuizQuestion>()
        repeat(learningSetsCount) {
            val randomizedGeneratedQuestionsSet = category.hiraganaList.shuffled().toMutableList()
                .map { hiragana -> QuizQuestion(question = hiragana) }
            generatedQuestions.lastOrNull()?.let { lastGeneratedQuestion ->
                if (randomizedGeneratedQuestionsSet.first() == lastGeneratedQuestion) {
                    Collections.swap(randomizedGeneratedQuestionsSet, 0, randomizedGeneratedQuestionsSet.lastIndex)
                }
            }

            generatedQuestions.addAll(randomizedGeneratedQuestionsSet)
        }

        setQuizQuestions(generatedQuestions)
    }

    open fun generateTestQuizQuestions(category: HiraganaCategory) {
        val hiraganaQuestions = category.complementedHiraganaList
        val quizQuestions = hiraganaQuestions.map { question -> QuizQuestion(question = question) }
        setQuizQuestions(quizQuestions.shuffled())
    }
}