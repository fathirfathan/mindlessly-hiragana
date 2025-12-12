package com.effatheresoft.mindlesslyhiragana.ui.quiz

import androidx.activity.ComponentActivity
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.AndroidComposeTestRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import com.effatheresoft.mindlesslyhiragana.data.Hiragana
import org.junit.rules.TestRule

class QuizScreenRobot <R : TestRule, A : ComponentActivity> (private val composeTestRule: AndroidComposeTestRule<R, A>) {
    val activity get() = composeTestRule.activity

    fun assert_onQuizScreen() {
        composeTestRule.onNodeWithText(activity.getString(com.effatheresoft.mindlesslyhiragana.R.string.remaining), substring = true).assertIsDisplayed()
    }

    fun click_navigateUpButton() {
        composeTestRule.onNodeWithContentDescription(activity.getString(com.effatheresoft.mindlesslyhiragana.R.string.navigate_back)).performClick()
    }

    fun click_answer(hiragana: Hiragana) {
        composeTestRule.onNodeWithText(hiragana.name).performClick()
    }

    fun click_answers(hiraganaList: List<Hiragana>) {
        hiraganaList.forEach {
            click_answer(it)
        }
    }
}