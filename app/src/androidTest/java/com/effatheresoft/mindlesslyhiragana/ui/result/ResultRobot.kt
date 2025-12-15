package com.effatheresoft.mindlesslyhiragana.ui.result

import androidx.activity.ComponentActivity
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertIsEnabled
import androidx.compose.ui.test.assertIsNotEnabled
import androidx.compose.ui.test.junit4.AndroidComposeTestRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import com.effatheresoft.mindlesslyhiragana.R
import com.effatheresoft.mindlesslyhiragana.data.repository.UserRepository
import org.junit.rules.TestRule

class ResultScreenRobot <TR : TestRule, CA : ComponentActivity> (
    private val composeTestRule: AndroidComposeTestRule<TR, CA>,
    private val userRepository: UserRepository
) {
    val activity get() = composeTestRule.activity

    suspend fun setLocalUserIsTestUnlocked(isUnlocked: Boolean) {
        userRepository.updateLocalUserIsTestUnlocked(isUnlocked)
    }

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

    fun assert_testAllLearnedButton_enabled() {
        composeTestRule.onNodeWithText(activity.getString(R.string.test_all_learned)).assertIsEnabled()
    }

    fun assert_testAllLearnedButton_disabled() {
        composeTestRule.onNodeWithText(activity.getString(R.string.test_all_learned)).assertIsNotEnabled()
    }

    fun click_testAllLearnedButton() {
        composeTestRule.onNodeWithText(activity.getString(R.string.test_all_learned)).performClick()
    }
}