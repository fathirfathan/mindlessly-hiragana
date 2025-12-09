package com.effatheresoft.mindlesslyhiragana.quiz

import com.effatheresoft.mindlesslyhiragana.Constants.DEFAULT_LEARNING_SETS_COUNT
import com.effatheresoft.mindlesslyhiragana.MainCoroutineRule
import com.effatheresoft.mindlesslyhiragana.data.Hiragana.HI
import com.effatheresoft.mindlesslyhiragana.data.Hiragana.KA
import com.effatheresoft.mindlesslyhiragana.data.Hiragana.MI
import com.effatheresoft.mindlesslyhiragana.data.Hiragana.SE
import com.effatheresoft.mindlesslyhiragana.data.HiraganaCategory.HIMIKASE
import com.effatheresoft.mindlesslyhiragana.data.repository.QuizRepository
import com.effatheresoft.mindlesslyhiragana.sharedtest.data.FakeQuizRepository
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class QuizViewModelTest {
    private lateinit var fakeQuizRepository: QuizRepository
    private lateinit var quizViewModel: QuizViewModel

    @OptIn(ExperimentalCoroutinesApi::class)
    @get:Rule
    val mainCoroutineRule = MainCoroutineRule()

    @Before
    fun setupViewModel() {
        fakeQuizRepository = FakeQuizRepository()
        quizViewModel = QuizViewModel(fakeQuizRepository)
    }

    @Test
    fun whenQuizIsGenerated_assertCurrentQuizState() = runTest {
        val categoryId = HIMIKASE.id
        val expectedCurrentQuiz = Quiz(
            question = HI,
            possibleAnswers = listOf(
                PossibleAnswer(answer = HI, isCorrect = true, isSelected = false),
                PossibleAnswer(answer = MI, isCorrect = false, isSelected = false),
                PossibleAnswer(answer = KA, isCorrect = false, isSelected = false),
                PossibleAnswer(answer = SE, isCorrect = false, isSelected = false),
            )
        )

        quizViewModel.generateQuizzes(categoryId)
        assertEquals(expectedCurrentQuiz, quizViewModel.uiState.first().currentQuiz)
    }

    @Test
    fun whenQuizIsGenerated_assertRemainingQuestionsCountState() = runTest {
        val categoryId = HIMIKASE.id
        val possibleCurrentQuestion = HIMIKASE.hiraganaList
        quizViewModel.generateQuizzes(categoryId)
        val remainingQuestionsCount = possibleCurrentQuestion.size * DEFAULT_LEARNING_SETS_COUNT - 1

        assertEquals(remainingQuestionsCount, quizViewModel.uiState.first().remainingQuestionsCount)
    }

    @Test
    fun onCurrentQuiz_whenAllAnswersAreSelected_assertAllAnswersAreSelected() = runTest {
        val categoryId = HIMIKASE.id
        val possibleAnswers = listOf(
            PossibleAnswer(HI, isCorrect = true, isSelected = false),
            PossibleAnswer(MI, isCorrect = false, isSelected = false),
            PossibleAnswer(KA, isCorrect = false, isSelected = false),
            PossibleAnswer(SE, isCorrect = false, isSelected = false)
        )
        quizViewModel.generateQuizzes(categoryId)

        quizViewModel.selectCurrentQuizAnswer(possibleAnswers[0].answer)
        quizViewModel.selectCurrentQuizAnswer(possibleAnswers[1].answer)
        quizViewModel.selectCurrentQuizAnswer(possibleAnswers[2].answer)
        quizViewModel.selectCurrentQuizAnswer(possibleAnswers[3].answer)
        assertEquals(true, quizViewModel.uiState.first().currentQuiz!!.possibleAnswers[0].isSelected)
        assertEquals(true, quizViewModel.uiState.first().currentQuiz!!.possibleAnswers[1].isSelected)
        assertEquals(true, quizViewModel.uiState.first().currentQuiz!!.possibleAnswers[2].isSelected)
        assertEquals(true, quizViewModel.uiState.first().currentQuiz!!.possibleAnswers[3].isSelected)
    }
}