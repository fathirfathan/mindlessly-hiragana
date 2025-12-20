package com.effatheresoft.mindlesslyhiragana.navigation

import androidx.activity.ComponentActivity
import com.effatheresoft.mindlesslyhiragana.data.model.HiraganaCategory
import com.effatheresoft.mindlesslyhiragana.data.repository.UserRepository
import com.effatheresoft.mindlesslyhiragana.ui.home.HomeScreenRobot
import com.effatheresoft.mindlesslyhiragana.ui.learn.LearnScreenRobot
import com.effatheresoft.mindlesslyhiragana.ui.quiz.QuizScreenRobot
import com.effatheresoft.mindlesslyhiragana.ui.result.ResultScreenRobot
import com.effatheresoft.mindlesslyhiragana.ui.test.TestScreenRobot
import com.effatheresoft.mindlesslyhiragana.ui.testquiz.TestQuizScreenRobot
import com.effatheresoft.mindlesslyhiragana.ui.testresult.TestResultScreenRobot
import junit.framework.TestCase.assertNotNull
import kotlinx.coroutines.flow.first
import org.junit.rules.TestRule

class ScreenRobot <R : TestRule, A : ComponentActivity> (
    val fakeUserRepository: UserRepository,
    val home: HomeScreenRobot <R,A>,
    val learn: LearnScreenRobot <R,A>,
    val quiz: QuizScreenRobot <R,A>,
    val result: ResultScreenRobot <R, A>,
    val test: TestScreenRobot<R, A>,
    val testQuiz: TestQuizScreenRobot<R, A>,
    val testResult: TestResultScreenRobot<R, A>
) {
    suspend fun setLocalUser(progress: String, learningSetsCount: Int, isTestUnlocked: Boolean) {
        fakeUserRepository.updateLocalUserProgress(progress)
        fakeUserRepository.updateLocalUserLearningSetsCount(learningSetsCount)
        fakeUserRepository.updateLocalUserIsTestUnlocked(isTestUnlocked)
    }

    fun navigate_homeToLearn(category: HiraganaCategory) {
        home.clickCategory(category)
        learn.category = category
        learn.assert_onLearnScreen()
    }

    fun navigate_homeToTest() {
        home.clickTestAllLearnedButton()
        test.assertOnTestScreen()
    }

    fun navigate_learnToQuiz() {
        learn.click_learnButton()
        quiz.assert_onQuizScreen()
    }

    fun navigate_quizToResult(isAllCorrect: Boolean) {
        if (isAllCorrect) {
            repeat(learn.progressBarValue) {
                quiz.click_answers(learn.category.hiraganaList)
            }
            result.assert_onResultScreen()
        } else {
            repeat(learn.progressBarValue - 1) {
                quiz.click_answers(learn.category.hiraganaList)
            }
            learn.category.hiraganaList.forEachIndexed { index, answer ->
                when(index) {
                    1 -> {
                        quiz.click_answer(learn.category.hiraganaList[0])
                        quiz.click_answer(answer)
                    }
                    else -> quiz.click_answer(answer)
                }
            }
            result.assert_onResultScreen()
        }
    }

    fun navigate_testToLearn() {
        test.challengeButton_click()
        learn.assert_onLearnScreen(test.categoryList.last())
    }

    fun navigateBack_learnToHome() {
        learn.click_navigateUpButton()
        home.assert_onHomeScreen()
    }

    fun navigateBack_quizToLearn() {
        quiz.click_navigateUpButton()
        learn.assert_onLearnScreen(learn.category)
    }

    fun navigateBack_testToHome() {
        test.topAppBarNavButton_click()
        home.assert_onHomeScreen()
    }

    fun navigate_testToTestQuiz() {
        test.testButton_click()
        testQuiz.assertOnTestQuizScreen()
    }

    suspend fun navigate_testQuizToTestResult(isAllAnswersCorrect: Boolean = true) {
        val answers = fakeUserRepository.observeLocalUser().first()
            .progress.toHiraganaCategory()?.hiraganaList
        assertNotNull(answers)

        if (isAllAnswersCorrect) {
            answers?.forEach { answer ->
                testQuiz.answerButton_click(answer)
            }
        } else {
            answers?.forEachIndexed { index, answer ->
                when (index) {
                    1 -> {
                        testQuiz.answerButton_click(answers[0])
                        testQuiz.answerButton_click(answer)
                    }
                    else -> testQuiz.answerButton_click(answer)
                }
            }
        }
        testResult.assert_onTestResultScreen()
    }

    fun String.toHiraganaCategory(): HiraganaCategory? {
        return HiraganaCategory.entries.firstOrNull { it.id == this }
    }
}