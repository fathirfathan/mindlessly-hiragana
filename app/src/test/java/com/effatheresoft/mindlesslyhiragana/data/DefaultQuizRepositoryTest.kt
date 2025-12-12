package com.effatheresoft.mindlesslyhiragana.data

import com.effatheresoft.mindlesslyhiragana.Constants.DEFAULT_LEARNING_SETS_COUNT
import com.effatheresoft.mindlesslyhiragana.MainCoroutineRule
import com.effatheresoft.mindlesslyhiragana.data.model.Hiragana
import com.effatheresoft.mindlesslyhiragana.data.model.HiraganaCategory
import com.effatheresoft.mindlesslyhiragana.data.repository.DefaultQuizRepository
import com.effatheresoft.mindlesslyhiragana.data.repository.QuizRepository
import com.effatheresoft.mindlesslyhiragana.ui.quiz.PossibleAnswer
import com.effatheresoft.mindlesslyhiragana.ui.quiz.Quiz
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class DefaultQuizRepositoryTest {
    private lateinit var quizRepository: QuizRepository

    @OptIn(ExperimentalCoroutinesApi::class)
    @get:Rule
    val mainCoroutineRule = MainCoroutineRule()

    @Before
    fun setupRepository() = runTest{
        quizRepository = DefaultQuizRepository(FakeUserDao())
        quizRepository.generateQuizzes(HiraganaCategory.HIMIKASE.id)
    }

    @Test
    fun generateQuizzes_assertQuizzesSize() = runTest {
        val quizzes = quizRepository.observeQuizzes().first()
        val expectedQuizzesSize = HiraganaCategory.HIMIKASE.hiraganaList.size * DEFAULT_LEARNING_SETS_COUNT

        assert(quizzes.isNotEmpty())
        assertEquals(expectedQuizzesSize, quizzes.size)
    }

    @Test
    fun selectQuizAnswer_assertAnswerIsSelected() = runTest {
        quizRepository.selectQuizAnswer(0, Hiragana.HI)
        quizRepository.selectQuizAnswer(0, Hiragana.MI)
        quizRepository.selectQuizAnswer(0, Hiragana.KA)
        quizRepository.selectQuizAnswer(0, Hiragana.SE)
        val quizzes = quizRepository.observeQuizzes().first()
        assertEquals(true, quizzes[0].possibleAnswers[0].isSelected)
        assertEquals(true, quizzes[0].possibleAnswers[1].isSelected)
        assertEquals(true, quizzes[0].possibleAnswers[2].isSelected)
        assertEquals(true, quizzes[0].possibleAnswers[3].isSelected)
    }

    @Test
    fun selectCorrectAnswer_assertCurrentQuizIsUpdated() = runTest {
        quizRepository.selectQuizAnswer(0, Hiragana.HI)
        val quizzes = quizRepository.observeQuizzes().first()

        val newQuiz = Quiz(
            question = Hiragana.MI,
            possibleAnswers = listOf(
                PossibleAnswer(answer = Hiragana.HI, isCorrect = false, isSelected = false),
                PossibleAnswer(answer = Hiragana.MI, isCorrect = true, isSelected = false),
                PossibleAnswer(answer = Hiragana.KA, isCorrect = false, isSelected = false),
                PossibleAnswer(answer = Hiragana.SE, isCorrect = false, isSelected = false)
            )
        )
        assertEquals(newQuiz, quizzes[1])
    }

    @Test
    fun selectCorrectAnswer_assertPreviousQuizIsSaved() = runTest {
        quizRepository.selectQuizAnswer(0, Hiragana.HI)
        val quizzes = quizRepository.observeQuizzes().first()

        val previousQuiz = Quiz(
            question = Hiragana.HI,
            possibleAnswers = listOf(
                PossibleAnswer(answer = Hiragana.HI, isCorrect = true, isSelected = true),
                PossibleAnswer(answer = Hiragana.MI, isCorrect = false, isSelected = false),
                PossibleAnswer(answer = Hiragana.KA, isCorrect = false, isSelected = false),
                PossibleAnswer(answer = Hiragana.SE, isCorrect = false, isSelected = false)
            )
        )
        assertEquals(previousQuiz, quizzes[0])
    }
}