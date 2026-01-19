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
    private val quizQuestions get() = _quizQuestions.value

    fun observeQuizQuestions(): StateFlow<List<QuizQuestion>> = _quizQuestions

    fun setQuizQuestions(questions: List<QuizQuestion>) {
        _quizQuestions.value = questions
    }

    fun selectAnswer(index: Int, answer: Hiragana) {
        val updatedQuestions = quizQuestions.toMutableList().apply {
            val targetQuestion = get(index)
            val updatedQuestion = targetQuestion.copy(answerAttempts = targetQuestion.answerAttempts + answer)
            set(index, updatedQuestion)
        }
        _quizQuestions.value = updatedQuestions
    }

    open fun generateLearnQuizQuestions(category: HiraganaCategory, repeatCategoryCount: Int) {
        val generatedQuestions = mutableListOf<QuizQuestion>()
        repeat(repeatCategoryCount) {
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