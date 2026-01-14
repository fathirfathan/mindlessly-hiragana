package com.effatheresoft.mindlesslyhiragana.data.repository

import com.effatheresoft.mindlesslyhiragana.data.local.UserDao
import com.effatheresoft.mindlesslyhiragana.data.model.Hiragana
import com.effatheresoft.mindlesslyhiragana.data.model.HiraganaCategory
import com.effatheresoft.mindlesslyhiragana.ui.quiz.PossibleAnswer
import com.effatheresoft.mindlesslyhiragana.ui.quiz.Quiz
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import java.util.Collections
import javax.inject.Inject
import kotlin.collections.map

open class QuizVolatileDataSource @Inject constructor() {
    private val _quizzes = MutableStateFlow(emptyList<Quiz>())
    fun observeQuizzes(): StateFlow<List<Quiz>> = _quizzes

    fun setQuizzes(quizzes: List<Quiz>) {
        _quizzes.value = quizzes
    }

    open fun generateQuizzes(
        categoryId: String,
        learningSetsCount: Int
    ) {
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
            val randomizedPossibleQuizzes = possibleQuizzes.shuffled().toMutableList()
            generatedQuizzes.lastOrNull()?.let { lastGeneratedQuiz ->
                if (randomizedPossibleQuizzes.first() == lastGeneratedQuiz) {
                    Collections.swap(randomizedPossibleQuizzes, 0, randomizedPossibleQuizzes.lastIndex)
                }
            }

            generatedQuizzes.addAll(randomizedPossibleQuizzes)
        }
        _quizzes.value = generatedQuizzes
    }
}

class RefactoredQuizRepository @Inject constructor(
    private val userLocalDataSource: UserDao,
    private val quizVolatileDataSource: QuizVolatileDataSource
) {
    private val observedLearningSetsCount = userLocalDataSource.observeLocalUser().map { it.learningSetsCount }
    private val _quizzes = quizVolatileDataSource.observeQuizzes()
    fun observeQuizzes(): StateFlow<List<Quiz>> = _quizzes

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

    suspend fun generateQuizzes(categoryId: String) {
        quizVolatileDataSource.generateQuizzes(categoryId, observedLearningSetsCount.first())
    }
}