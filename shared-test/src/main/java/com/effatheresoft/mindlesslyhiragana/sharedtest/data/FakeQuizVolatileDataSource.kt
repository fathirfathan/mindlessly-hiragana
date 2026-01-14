package com.effatheresoft.mindlesslyhiragana.sharedtest.data

import com.effatheresoft.mindlesslyhiragana.data.model.HiraganaCategory
import com.effatheresoft.mindlesslyhiragana.data.repository.QuizVolatileDataSource
import com.effatheresoft.mindlesslyhiragana.ui.quiz.PossibleAnswer
import com.effatheresoft.mindlesslyhiragana.ui.quiz.Quiz

class FakeQuizVolatileDataSource: QuizVolatileDataSource() {
    override fun generateQuizzes(categoryId: String, learningSetsCount: Int) {
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
        repeat(learningSetsCount) {
            generatedQuizzes.addAll(possibleQuizzes)
        }
        setQuizzes(generatedQuizzes)
    }
}