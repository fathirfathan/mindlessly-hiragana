package com.effatheresoft.mindlesslyhiragana.quiz

import com.effatheresoft.mindlesslyhiragana.Constants.DEFAULT_LEARNING_SETS_COUNT
import com.effatheresoft.mindlesslyhiragana.MainCoroutineRule
import com.effatheresoft.mindlesslyhiragana.data.FakeUserRepository
import com.effatheresoft.mindlesslyhiragana.data.Hiragana.HI
import com.effatheresoft.mindlesslyhiragana.data.Hiragana.KA
import com.effatheresoft.mindlesslyhiragana.data.Hiragana.MI
import com.effatheresoft.mindlesslyhiragana.data.Hiragana.SE
import com.effatheresoft.mindlesslyhiragana.data.HiraganaCategory.HIMIKASE
import com.effatheresoft.mindlesslyhiragana.data.UserRepository
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class QuizViewModelTest {
    private lateinit var fakeUserRepository: UserRepository
    private lateinit var quizViewModel: QuizViewModel

    @OptIn(ExperimentalCoroutinesApi::class)
    @get:Rule
    val mainCoroutineRule = MainCoroutineRule()

    @Before
    fun setupViewModel() {
        fakeUserRepository = FakeUserRepository()
        quizViewModel = QuizViewModel(fakeUserRepository)
    }

    @Test
    fun givenQuizIsGenerated_assertCurrentQuestionState() = runTest {
        val categoryId = HIMIKASE.id
        val possibleCurrentQuestion = listOf(HI.kana, MI.kana, KA.kana, SE.kana)
        quizViewModel.generateQuiz(categoryId)

        assertTrue(possibleCurrentQuestion.contains(quizViewModel.uiState.first().currentQuestion))
    }

    @Test
    fun givenQuizIsGenerated_assertRemainingQuestionsCountState() = runTest {
        val categoryId = HIMIKASE.id
        val possibleCurrentQuestion = listOf(HI.kana, MI.kana, KA.kana, SE.kana)
        quizViewModel.generateQuiz(categoryId)
        val remainingQuestionsCount = possibleCurrentQuestion.size * DEFAULT_LEARNING_SETS_COUNT - 1

        assertEquals(remainingQuestionsCount, quizViewModel.uiState.first().remainingQuestionsCount)
    }

    @Test
    fun givenQuizIsGenerated_assertPossibleAnswersState() = runTest {
        val categoryId = HIMIKASE.id
        val possibleAnswers = listOf(HI.name, MI.name, KA.name, SE.name)
        quizViewModel.generateQuiz(categoryId)

        assertEquals(possibleAnswers, quizViewModel.uiState.first().possibleAnswers)
    }
}