package com.effatheresoft.mindlesslyhiragana.ui.testresult

import androidx.activity.ComponentActivity
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertIsEnabled
import androidx.compose.ui.test.assertIsNotEnabled
import androidx.compose.ui.test.hasContentDescription
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.junit4.AndroidComposeTestRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import com.effatheresoft.mindlesslyhiragana.R
import com.effatheresoft.mindlesslyhiragana.data.model.Hiragana
import com.effatheresoft.mindlesslyhiragana.data.repository.UserRepository
import com.effatheresoft.mindlesslyhiragana.ui.learn.isButton
import org.junit.rules.TestRule

class TestResultScreenRobot <TR : TestRule, CA : ComponentActivity> (
    private val composeTestRule: AndroidComposeTestRule<TR, CA>,
    private val userRepository: UserRepository
) {
    private val activity get() = composeTestRule.activity

    fun assert_onTestResultScreen() {
        topAppBarTitle_assertIsDisplayed()
        continueLearningButton_assertIsDisplayed()
    }

    suspend fun setCanContinueLearning(canContinueLearning: Boolean) {
        userRepository.updateLocalUserIsTestUnlocked(!canContinueLearning)
    }

    fun topAppBarTitle_assertIsDisplayed() {
        composeTestRule.onNodeWithText(activity.getString(R.string.result)).assertIsDisplayed()
    }

    fun topAppBarNavButton_assertIsDisplayed() {
        composeTestRule.onNode(hasContentDescription(activity.getString(R.string.navigate_back))).assertIsDisplayed()
    }

    fun tryAgainButton_assertIsDisplayed() {
        composeTestRule.onNode(isButton() and hasText(activity.getString(R.string.try_again))).assertIsDisplayed()
    }

    fun continueLearningButton_assertIsDisplayed() {
        composeTestRule.onNode(isButton() and hasText(activity.getString(R.string.continue_learning))).assertIsDisplayed()
    }

    fun correctCountText_assertIsDisplayed(correctCount: Int) {
        composeTestRule.onNodeWithText(activity.getString(R.string.correct_n, correctCount)).assertIsDisplayed()
    }

    fun incorrectCountText_assertIsDisplayed(incorrectCount: Int) {
        composeTestRule.onNodeWithText(activity.getString(R.string.incorrect_n, incorrectCount)).assertIsDisplayed()
    }

    fun continueLearningButton_assertIsDisabled() {
        composeTestRule.onNode(isButton() and hasText(activity.getString(R.string.continue_learning))).assertIsNotEnabled()
    }

    fun continueLearningButton_assertIsEnabled() {
        composeTestRule.onNode(isButton() and hasText(activity.getString(R.string.continue_learning))).assertIsEnabled()
    }

    fun incorrectHiraganaList_assertIsDisplayed(incorrectHiraganaList: List<Hiragana>) {
        composeTestRule.onNodeWithText(activity.getString(R.string.incorrect_hiragana)).assertIsDisplayed()
        incorrectHiraganaList.chunked(4).forEach { fourHiragana ->
            val row = fourHiragana.joinToString(", ") {
                hiragana -> "${hiragana.kana}(${hiragana.name})"
            }.trim()
            composeTestRule.onNodeWithText(row).assertIsDisplayed()
        }
    }

    fun tryAgainButton_click() {
        composeTestRule.onNode(isButton() and hasText(activity.getString(R.string.try_again))).performClick()
    }

    fun topAppBarNavButton_click() {
        composeTestRule.onNode(hasContentDescription(activity.getString(R.string.navigate_back))).performClick()
    }

    fun continueLearningButton_click() {
        composeTestRule.onNode(isButton() and hasText(activity.getString(R.string.continue_learning))).performClick()
    }
}