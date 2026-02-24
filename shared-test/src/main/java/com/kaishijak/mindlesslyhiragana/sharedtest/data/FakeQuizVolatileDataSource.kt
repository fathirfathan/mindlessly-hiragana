package com.kaishijak.mindlesslyhiragana.sharedtest.data

import com.kaishijak.mindlesslyhiragana.data.model.HiraganaCategory
import com.kaishijak.mindlesslyhiragana.data.repository.QuizVolatileDataSource
import com.kaishijak.mindlesslyhiragana.ui.testquiz.QuizQuestion

class FakeQuizVolatileDataSource: QuizVolatileDataSource() {
    override fun generateTestQuizQuestions(category: HiraganaCategory) {
        val hiraganaQuestions = category.complementedHiraganaList
        val quizQuestions = hiraganaQuestions.map { question -> QuizQuestion(question = question) }
        setQuizQuestions(quizQuestions)
    }

    override fun generateLearnQuizQuestions(category: HiraganaCategory, repeatCategoryCount: Int) {
        val generatedQuestions = mutableListOf<QuizQuestion>()
        repeat(repeatCategoryCount) {
            val unrandomizedGeneratedQuestionsSet = category.hiraganaList.toMutableList()
                .map { hiragana -> QuizQuestion(question = hiragana) }

            generatedQuestions.addAll(unrandomizedGeneratedQuestionsSet)
        }

        setQuizQuestions(generatedQuestions)
    }
}