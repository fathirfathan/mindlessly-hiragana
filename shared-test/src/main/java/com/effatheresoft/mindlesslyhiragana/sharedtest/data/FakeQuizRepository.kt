package com.effatheresoft.mindlesslyhiragana.sharedtest.data

import com.effatheresoft.mindlesslyhiragana.Constants.DEFAULT_LEARNING_SETS_COUNT
import com.effatheresoft.mindlesslyhiragana.data.Hiragana
import com.effatheresoft.mindlesslyhiragana.data.HiraganaCategory
import com.effatheresoft.mindlesslyhiragana.data.repository.QuizRepository
import com.effatheresoft.mindlesslyhiragana.quiz.PossibleAnswer
import com.effatheresoft.mindlesslyhiragana.quiz.Quiz
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class FakeQuizRepository: QuizRepository {

    private var _currentHiraganaCategory: HiraganaCategory = HiraganaCategory.HIMIKASE
    private val observedLearningSetsCount = DEFAULT_LEARNING_SETS_COUNT
    private val _quizzes = MutableStateFlow(emptyList<Quiz>())
    override fun observeQuizzes(): StateFlow<List<Quiz>> = _quizzes

    override fun selectQuizAnswer(
        selectedQuizIndex: Int,
        selectedAnswer: Hiragana
    ) {
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
        _currentHiraganaCategory.let { hiraganaCategory ->
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
            val learningSetsCount = observedLearningSetsCount
            repeat(learningSetsCount) {
                generatedQuizzes.addAll(possibleQuizzes)
            }
            _quizzes.value = generatedQuizzes
        }
    }
}