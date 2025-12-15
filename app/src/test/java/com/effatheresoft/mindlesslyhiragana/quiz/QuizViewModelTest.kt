package com.effatheresoft.mindlesslyhiragana.quiz

import com.effatheresoft.mindlesslyhiragana.Constants.DEFAULT_LEARNING_SETS_COUNT
import com.effatheresoft.mindlesslyhiragana.MainCoroutineRule
import com.effatheresoft.mindlesslyhiragana.data.FakeUserRepository
import com.effatheresoft.mindlesslyhiragana.data.model.Hiragana
import com.effatheresoft.mindlesslyhiragana.data.model.Hiragana.HI
import com.effatheresoft.mindlesslyhiragana.data.model.Hiragana.KA
import com.effatheresoft.mindlesslyhiragana.data.model.Hiragana.MI
import com.effatheresoft.mindlesslyhiragana.data.model.Hiragana.SE
import com.effatheresoft.mindlesslyhiragana.data.model.HiraganaCategory
import com.effatheresoft.mindlesslyhiragana.data.model.HiraganaCategory.FUWOYA
import com.effatheresoft.mindlesslyhiragana.data.model.HiraganaCategory.HIMIKASE
import com.effatheresoft.mindlesslyhiragana.data.repository.QuizRepository
import com.effatheresoft.mindlesslyhiragana.data.repository.UserRepository
import com.effatheresoft.mindlesslyhiragana.sharedtest.data.FakeQuizRepository
import com.effatheresoft.mindlesslyhiragana.ui.quiz.PossibleAnswer
import com.effatheresoft.mindlesslyhiragana.ui.quiz.Quiz
import com.effatheresoft.mindlesslyhiragana.ui.quiz.QuizViewModel
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class QuizViewModelTest {
    private lateinit var fakeQuizRepository: QuizRepository
    private lateinit var fakeUserRepository: UserRepository
    private lateinit var quizViewModel: QuizViewModel

    @OptIn(ExperimentalCoroutinesApi::class)
    @get:Rule
    val mainCoroutineRule = MainCoroutineRule()

    @Before
    fun setupViewModel() {
        fakeQuizRepository = FakeQuizRepository()
        fakeUserRepository = FakeUserRepository()
        quizViewModel = QuizViewModel(fakeQuizRepository, fakeUserRepository)

        quizViewModel.generateQuizzes(HIMIKASE.id)
    }

    @Test
    fun whenQuizIsGenerated_assertCurrentQuizState() = runTest {
        val expectedCurrentQuiz = Quiz(
            question = HI,
            possibleAnswers = listOf(
                PossibleAnswer(answer = HI, isCorrect = true, isSelected = false),
                PossibleAnswer(answer = MI, isCorrect = false, isSelected = false),
                PossibleAnswer(answer = KA, isCorrect = false, isSelected = false),
                PossibleAnswer(answer = SE, isCorrect = false, isSelected = false),
            )
        )
        assertEquals(expectedCurrentQuiz, quizViewModel.uiState.first().currentQuiz)
    }

    @Test
    fun whenQuizIsGenerated_assertRemainingQuestionsCountState() = runTest {
        val possibleCurrentQuestion = HIMIKASE.hiraganaList
        val remainingQuestionsCount = possibleCurrentQuestion.size * DEFAULT_LEARNING_SETS_COUNT - 1

        assertEquals(remainingQuestionsCount, quizViewModel.uiState.first().remainingQuestionsCount)
    }

    @Test
    fun onCurrentQuiz_whenIncorrectAnswersAreSelected_assertAreSelected() = runTest {
        val possibleAnswers = listOf(
            PossibleAnswer(HI, isCorrect = true, isSelected = false),
            PossibleAnswer(MI, isCorrect = false, isSelected = false),
            PossibleAnswer(KA, isCorrect = false, isSelected = false),
            PossibleAnswer(SE, isCorrect = false, isSelected = false)
        )

        quizViewModel.selectCurrentQuizAnswer(possibleAnswers[1].answer)
        quizViewModel.selectCurrentQuizAnswer(possibleAnswers[2].answer)
        quizViewModel.selectCurrentQuizAnswer(possibleAnswers[3].answer)
        assertEquals(true, quizViewModel.uiState.first().currentQuiz!!.possibleAnswers[1].isSelected)
        assertEquals(true, quizViewModel.uiState.first().currentQuiz!!.possibleAnswers[2].isSelected)
        assertEquals(true, quizViewModel.uiState.first().currentQuiz!!.possibleAnswers[3].isSelected)
    }

    @Test
    fun onCurrentQuiz_whenCorrectAnswerIsSelected_assertCurrentQuizIsUpdated() = runTest {
        val possibleAnswers = listOf(
            PossibleAnswer(HI, isCorrect = true, isSelected = false),
            PossibleAnswer(MI, isCorrect = false, isSelected = false),
            PossibleAnswer(KA, isCorrect = false, isSelected = false),
            PossibleAnswer(SE, isCorrect = false, isSelected = false)
        )
        val expectedUpdatedQuiz = Quiz(
            question = MI,
            possibleAnswers = listOf(
                PossibleAnswer(HI, isCorrect = false, isSelected = false),
                PossibleAnswer(MI, isCorrect = true, isSelected = false),
                PossibleAnswer(KA, isCorrect = false, isSelected = false),
                PossibleAnswer(SE, isCorrect = false, isSelected = false)
            )
        )
        val expectedRemainingQuestionsCount = HIMIKASE.hiraganaList.size * DEFAULT_LEARNING_SETS_COUNT - 2

        quizViewModel.selectCurrentQuizAnswer(possibleAnswers[0].answer)
        assertEquals(expectedUpdatedQuiz, quizViewModel.uiState.first().currentQuiz)
        assertEquals(expectedRemainingQuestionsCount, quizViewModel.uiState.first().remainingQuestionsCount)
    }

    @Test
    fun whenAllQuizzesAreCorrectlyAnswered_assertIsCompleted() = runTest {
        val correctAnswers = mutableListOf<Hiragana>()
        repeat(DEFAULT_LEARNING_SETS_COUNT) {
            correctAnswers.addAll(listOf(HI, MI, KA, SE))
        }
        correctAnswers.forEach { answer ->
            quizViewModel.selectCurrentQuizAnswer(answer)
        }

        assertEquals(true, quizViewModel.uiState.first().isCompleted)
    }

    @Test
    fun onLocalUserProgressCategory_whenAllQuizzesAreCorrectlyAnswered_assertLocalUserIsTestUnlockedTrue() = runTest {
        answerAllQuizzesCorrectly(HIMIKASE)

        assertEquals(true, fakeUserRepository.observeLocalUser().first().isTestUnlocked)
    }

    @Test
    fun onLocalUserProgressCategory_whenQuizzesHaveIncorrectAnswer_assertLocalUserIsTestUnlockedFalse() = runTest {
        val answers = mutableListOf<Hiragana>()
        repeat(DEFAULT_LEARNING_SETS_COUNT - 1) {
            answers.addAll(HIMIKASE.hiraganaList)
        }
        selectAnswers(listOf(HI, MI, MI, KA, SE))

        assertEquals(false, fakeUserRepository.observeLocalUser().first().isTestUnlocked)
    }

    @Test
    fun notOnLocalUserProgressCategory_whenAllQuizzesAreCorrectlyAnswered_assertLocalUserIsTestUnlockedFalse() = runTest {
        fakeUserRepository.updateLocalUserProgress(FUWOYA.id)
        answerAllQuizzesCorrectly(HIMIKASE)

        assertEquals(false, fakeUserRepository.observeLocalUser().first().isTestUnlocked)
    }

    fun answerAllQuizzesCorrectly(category: HiraganaCategory) {
        val correctAnswers = mutableListOf<Hiragana>()
        repeat(DEFAULT_LEARNING_SETS_COUNT) {
            correctAnswers.addAll(category.hiraganaList)
        }
        correctAnswers.forEach { answer ->
            quizViewModel.selectCurrentQuizAnswer(answer)
        }
    }

    fun selectAnswers(answers: List<Hiragana>) {
        answers.forEach { answer ->
            quizViewModel.selectCurrentQuizAnswer(answer)
        }
    }
}