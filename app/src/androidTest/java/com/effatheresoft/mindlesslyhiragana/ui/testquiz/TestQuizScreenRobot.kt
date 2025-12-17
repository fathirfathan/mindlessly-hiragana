package com.effatheresoft.mindlesslyhiragana.ui.testquiz

import androidx.activity.ComponentActivity
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertIsEnabled
import androidx.compose.ui.test.assertIsNotEnabled
import androidx.compose.ui.test.hasContentDescription
import androidx.compose.ui.test.hasScrollToKeyAction
import androidx.compose.ui.test.junit4.AndroidComposeTestRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performScrollToKey
import com.effatheresoft.mindlesslyhiragana.R
import com.effatheresoft.mindlesslyhiragana.data.model.Hiragana
import com.effatheresoft.mindlesslyhiragana.data.repository.UserRepository
import org.junit.rules.TestRule

class TestQuizScreenRobot<TR : TestRule, CA : ComponentActivity> (
    private val composeTestRule: AndroidComposeTestRule<TR, CA>,
    private val userRepository: UserRepository
) {

    private val activity get() = composeTestRule.activity

    fun assertOnTestQuizScreen() {
        topAppBarTitle_assertIsDisplayed()
        remainingText_assertIsDisplayed()
    }

    fun topAppBarTitle_assertIsDisplayed() {
        composeTestRule.onNodeWithText(activity.getString(R.string.test_all_learned)).assertIsDisplayed()
    }

    fun topAppBarNavButton_assertIsDisplayed() {
        composeTestRule.onNode(hasContentDescription(activity.getString(R.string.navigate_back))).assertIsDisplayed()
    }

    fun topAppBarNavButton_click() {
        composeTestRule.onNode(hasContentDescription(activity.getString(R.string.navigate_back))).performClick()
    }

    fun questionText_assertIsDisplayed(question: String) {
        composeTestRule.onNodeWithText(question).assertIsDisplayed()
    }

    fun remainingText_assertIsDisplayed(count: Int? = null) {
        composeTestRule.run {
            when(count) {
                null -> onNodeWithText(activity.getString(R.string.remaining), substring = true)
                else -> onNodeWithText(activity.getString(R.string.remaining_n, count))
            }
        }.assertIsDisplayed()
    }

    fun answerButtons_assertAreDisplayed() {
        val answerButtons = Hiragana.entries
        answerButtons.forEach { answer ->
            hiraganaKeyboard_scrollToAnswer(answer)
            composeTestRule.onNodeWithText(answer.name).assertIsDisplayed()
        }
    }

    fun answerButton_assertIsEnabled(answer: Hiragana) {
        hiraganaKeyboard_scrollToAnswer(answer)
        composeTestRule.onNodeWithText(answer.name).assertIsEnabled()
    }

    fun answerButton_assertIsDisabled(answer: Hiragana) {
        hiraganaKeyboard_scrollToAnswer(answer)
        composeTestRule.onNodeWithText(answer.name).assertIsNotEnabled()
    }

    fun answerButton_click(answer: Hiragana) {
        hiraganaKeyboard_scrollToAnswer(answer)
        composeTestRule.onNodeWithText(answer.name).performClick()
    }

    fun hiraganaKeyboard_scrollToAnswer(answer: Hiragana) {
        composeTestRule.onNode(hasScrollToKeyAction()).performScrollToKey(answer)
    }
}