package com.effatheresoft.mindlesslyhiragana.navigation

import androidx.activity.ComponentActivity
import com.effatheresoft.mindlesslyhiragana.data.model.HiraganaCategory
import com.effatheresoft.mindlesslyhiragana.ui.home.HomeScreenRobot
import com.effatheresoft.mindlesslyhiragana.ui.learn.LearnScreenRobot
import com.effatheresoft.mindlesslyhiragana.ui.quiz.QuizScreenRobot
import com.effatheresoft.mindlesslyhiragana.ui.result.ResultScreenRobot
import org.junit.rules.TestRule

class ScreenRobot <R : TestRule, A : ComponentActivity> (
    val home: HomeScreenRobot <R,A>,
    val learn: LearnScreenRobot <R,A>,
    val quiz: QuizScreenRobot <R,A>,
    val result: ResultScreenRobot <R, A>
) {
    fun navigate_homeToLearn(category: HiraganaCategory) {
        home.clickCategory(category)
        learn.category = category
        learn.assert_onLearnScreen()
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

    fun navigateBack_learnToHome() {
        learn.click_navigateUpButton()
        home.assert_onHomeScreen()
    }

    fun navigateBack_quizToLearn() {
        quiz.click_navigateUpButton()
        learn.assert_onLearnScreen(learn.category)
    }
}