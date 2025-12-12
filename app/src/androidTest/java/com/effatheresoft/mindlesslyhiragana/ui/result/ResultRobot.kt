package com.effatheresoft.mindlesslyhiragana.ui.result

import androidx.activity.ComponentActivity
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.AndroidComposeTestRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import org.junit.rules.TestRule

class ResultScreenRobot <R : TestRule, A : ComponentActivity> (private val composeTestRule: AndroidComposeTestRule<R, A>) {
    val activity get() = composeTestRule.activity

    fun assert_onResultScreen() {
        composeTestRule.onNodeWithText(activity.getString(com.effatheresoft.mindlesslyhiragana.R.string.result)).assertIsDisplayed()
    }

    fun click_navigateUpButton() {
        composeTestRule.onNodeWithContentDescription(activity.getString(com.effatheresoft.mindlesslyhiragana.R.string.navigate_back)).performClick()
    }

    fun assert_correctCount_displayed(value: Int) {
        composeTestRule.onNodeWithText("Correct: $value").assertIsDisplayed()
    }

    fun assert_incorrectCount_displayed(value: Int) {
        composeTestRule.onNodeWithText("Incorrect: $value").assertIsDisplayed()
    }
}