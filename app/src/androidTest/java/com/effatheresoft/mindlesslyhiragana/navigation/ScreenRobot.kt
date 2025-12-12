package com.effatheresoft.mindlesslyhiragana.navigation

import androidx.activity.ComponentActivity
import com.effatheresoft.mindlesslyhiragana.data.model.HiraganaCategory
import com.effatheresoft.mindlesslyhiragana.ui.home.HomeScreenRobot
import com.effatheresoft.mindlesslyhiragana.ui.learn.LearnScreenRobot
import com.effatheresoft.mindlesslyhiragana.ui.quiz.QuizScreenRobot
import com.effatheresoft.mindlesslyhiragana.ui.result.ResultScreenRobot
import com.effatheresoft.mindlesslyhiragana.ui.test.TestScreenRobot
import org.junit.rules.TestRule

class ScreenRobot <R : TestRule, A : ComponentActivity> (
    val home: HomeScreenRobot <R,A>,
    val learn: LearnScreenRobot <R,A>,
    val quiz: QuizScreenRobot <R,A>,
    val result: ResultScreenRobot <R, A>,
    val test: TestScreenRobot<R, A>,
) {
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
        }
    }

//    fun navigate_testToLearn() {
//        test.challengeButton_click()
//        learn.assert_onLearnScreen(test.categoryList.last())
//    }

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
}