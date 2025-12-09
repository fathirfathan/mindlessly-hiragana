package com.effatheresoft.mindlesslyhiragana.data

import com.effatheresoft.mindlesslyhiragana.Constants.DEFAULT_LEARNING_SETS_COUNT
import com.effatheresoft.mindlesslyhiragana.MainCoroutineRule
import com.effatheresoft.mindlesslyhiragana.data.repository.DefaultQuizRepository
import com.effatheresoft.mindlesslyhiragana.data.repository.QuizRepository
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
    fun setupRepository() {
        quizRepository = DefaultQuizRepository(FakeUserDao())
    }

    @Test
    fun generateQuizzes_assertQuizzesSize() = runTest {
        quizRepository.generateQuizzes(HiraganaCategory.HIMIKASE.id)
        val quizzes = quizRepository.observeQuizzes().first()
        val expectedQuizzesSize = HiraganaCategory.HIMIKASE.hiraganaList.size * DEFAULT_LEARNING_SETS_COUNT

        assert(quizzes.isNotEmpty())
        assertEquals(expectedQuizzesSize, quizzes.size)
    }

    @Test
    fun selectQuizAnswer_assertAnswerIsSelected() = runTest {
        quizRepository.generateQuizzes(HiraganaCategory.HIMIKASE.id)
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
}