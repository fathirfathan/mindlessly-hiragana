package com.effatheresoft.mindlesslyhiragana.sharedtest.data

import com.effatheresoft.mindlesslyhiragana.data.model.HiraganaCategory
import com.effatheresoft.mindlesslyhiragana.data.repository.QuizVolatileDataSource
import com.effatheresoft.mindlesslyhiragana.ui.testquiz.QuizQuestion

class FakeQuizVolatileDataSource: QuizVolatileDataSource() {
    override fun generateTestQuizQuestions(category: HiraganaCategory) {
        val hiraganaQuestions = category.complementedHiraganaList
        val quizQuestions = hiraganaQuestions.map { question -> QuizQuestion(question = question) }
        setQuizQuestions(quizQuestions)
    }

    override fun generateLearnQuizQuestions(category: HiraganaCategory, learningSetsCount: Int) {
        val generatedQuestions = mutableListOf<QuizQuestion>()
        repeat(learningSetsCount) {
            val unrandomizedGeneratedQuestionsSet = category.hiraganaList.toMutableList()
                .map { hiragana -> QuizQuestion(question = hiragana) }

            generatedQuestions.addAll(unrandomizedGeneratedQuestionsSet)
        }

        setQuizQuestions(generatedQuestions)
    }
}