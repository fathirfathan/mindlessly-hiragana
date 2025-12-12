package com.effatheresoft.mindlesslyhiragana.data.repository

import com.effatheresoft.mindlesslyhiragana.data.model.Hiragana
import com.effatheresoft.mindlesslyhiragana.data.model.HiraganaCategory
import com.effatheresoft.mindlesslyhiragana.data.local.UserDao
import com.effatheresoft.mindlesslyhiragana.ui.quiz.PossibleAnswer
import com.effatheresoft.mindlesslyhiragana.ui.quiz.Quiz
import jakarta.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map

class DefaultQuizRepository @Inject constructor(
    private val localDataSource: UserDao
): QuizRepository {

    private var _currentHiraganaCategory: HiraganaCategory? = null
    private val observedLearningSetsCount = localDataSource.observeLocalUser().map { it.learningSetsCount }

    private val _quizzes = MutableStateFlow(emptyList<Quiz>())
    override fun observeQuizzes(): StateFlow<List<Quiz>> = _quizzes

    override fun selectQuizAnswer(selectedQuizIndex: Int, selectedAnswer: Hiragana) {
        val quizzes = _quizzes.value.toMutableList()
        if (selectedQuizIndex in quizzes.indices) {
            val currentQuiz = quizzes[selectedQuizIndex]
            quizzes[selectedQuizIndex] = currentQuiz.copy(
                possibleAnswers = currentQuiz.possibleAnswers.map {
                    if (it.answer == selectedAnswer) it.copy(isSelected = true) else it
                }
            )
            _quizzes.value = quizzes
        }
    }

    override suspend fun generateQuizzes(categoryId: String) {
        _currentHiraganaCategory = HiraganaCategory.entries.first { it.id == categoryId }
        _currentHiraganaCategory?.let { hiraganaCategory ->
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
            val learningSetsCount = observedLearningSetsCount.first()
            repeat(learningSetsCount) {
                generatedQuizzes.addAll(possibleQuizzes)
            }
            _quizzes.value = generatedQuizzes
        }
    }
}