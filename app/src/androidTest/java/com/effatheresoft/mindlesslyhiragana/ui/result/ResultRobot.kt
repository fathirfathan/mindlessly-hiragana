package com.effatheresoft.mindlesslyhiragana.ui.result

import androidx.activity.ComponentActivity
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.AndroidComposeTestRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import com.effatheresoft.mindlesslyhiragana.R
import org.junit.rules.TestRule

class ResultScreenRobot <TR : TestRule, CA : ComponentActivity> (private val composeTestRule: AndroidComposeTestRule<TR, CA>) {
    val activity get() = composeTestRule.activity

    fun assert_onResultScreen() {
        composeTestRule.onNodeWithText(activity.getString(R.string.result)).assertIsDisplayed()
    }

    fun click_navigateUpButton() {
        composeTestRule.onNodeWithContentDescription(activity.getString(R.string.navigate_back)).performClick()
    }

    fun click_tryAgainButton() {
        composeTestRule.onNodeWithText(activity.getString(R.string.try_again)).performClick()
    }

    fun assert_correctCount_displayed(value: Int) {
        composeTestRule.onNodeWithText(activity.getString(R.string.correct_n, value)).assertIsDisplayed()
    }

    fun assert_incorrectCount_displayed(value: Int) {
        composeTestRule.onNodeWithText(activity.getString(R.string.incorrect_n, value)).assertIsDisplayed()
    }
}