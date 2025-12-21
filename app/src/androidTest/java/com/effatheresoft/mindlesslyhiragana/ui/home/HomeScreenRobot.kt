package com.effatheresoft.mindlesslyhiragana.ui.home

import androidx.activity.ComponentActivity
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertIsNotDisplayed
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.junit4.AndroidComposeTestRule
import androidx.compose.ui.test.onAllNodesWithText
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import com.effatheresoft.mindlesslyhiragana.R
import com.effatheresoft.mindlesslyhiragana.data.model.HiraganaCategory
import com.effatheresoft.mindlesslyhiragana.data.repository.UserRepository
import com.effatheresoft.mindlesslyhiragana.ui.learn.isButton
import org.junit.rules.TestRule

class HomeScreenRobot <TR : TestRule, CA : ComponentActivity> (
    private val composeTestRule: AndroidComposeTestRule<TR, CA>,
    private val userRepository: UserRepository
) {
    val activity get() = composeTestRule.activity

    suspend fun setLocalUserProgress(progress: String) {
        userRepository.updateLocalUserProgress(progress)
    }

    suspend fun setLocalUserLearningSetsCount(learningSetsCount: Int) {
        userRepository.updateLocalUserLearningSetsCount(learningSetsCount)
    }

    suspend fun setIsTestUnlocked(isTestUnlocked: Boolean) {
        userRepository.updateLocalUserIsTestUnlocked(isTestUnlocked)
    }

    fun clickCategory(category: HiraganaCategory) {
        composeTestRule.onNodeWithText(category.kanaWithNakaguro).performClick()
    }

    fun assert_onHomeScreen() {
        composeTestRule.onAllNodesWithText(activity.getString(R.string.mindlessly_hiragana))[0].assertIsDisplayed()
    }

    fun clickTestAllLearnedButton() {
        composeTestRule.onNode(isButton() and hasText(activity.getString(R.string.test_all_learned))).performClick()
    }

    fun topAppBarMenu_assertIsDisplayed() {
        composeTestRule.onNodeWithContentDescription(activity.getString(R.string.open_menu)).assertIsDisplayed()
    }

    fun topAppBarMenu_click() {
        composeTestRule.onNodeWithContentDescription(activity.getString(R.string.open_menu)).performClick()
    }

    fun drawer_assertIsDisplayed() {
        composeTestRule.onNodeWithText(activity.getString(R.string.reset_progress)).assertIsDisplayed()
    }

    fun drawerTitleText_assertIsDisplayed() {
        composeTestRule.onAllNodesWithText(activity.getString(R.string.mindlessly_hiragana))[1].assertIsDisplayed()
    }

    fun drawerResetProgressButton_assertIsDisplayed() {
        composeTestRule.onNodeWithText(activity.getString(R.string.reset_progress)).assertIsDisplayed()
    }

    fun drawerResetProgressButton_click() {
        composeTestRule.onNodeWithText(activity.getString(R.string.reset_progress)).performClick()
    }

    fun resetDialog_assertIsDisplayed() {
        composeTestRule.onNode(isButton() and hasText(activity.getString(R.string.reset))).assertIsDisplayed()
        composeTestRule.onNode(isButton() and hasText(activity.getString(R.string.cancel))).assertIsDisplayed()
    }

    fun resetDialogTitleText_assertIsDisplayed() {
        composeTestRule.onAllNodesWithText(activity.getString(R.string.reset_progress))[1].assertIsDisplayed()
    }

    fun resetDialogText_assertIsDisplayed() {
        composeTestRule.onNodeWithText(activity.getString(R.string.reset_dialog_text)).assertIsDisplayed()
    }

    fun resetDialogConfirmButton_assertIsDisplayed() {
        composeTestRule.onNode(isButton() and hasText(activity.getString(R.string.reset))).assertIsDisplayed()
    }

    fun resetDialogCancelButton_assertIsDisplayed() {
        composeTestRule.onNode(isButton() and hasText(activity.getString(R.string.cancel))).assertIsDisplayed()
    }

    fun resetDialogCancelButton_click() {
        composeTestRule.onNode(isButton() and hasText(activity.getString(R.string.cancel))).performClick()
    }

    fun resetDialog_assertIsNotDisplayed() {
        composeTestRule.onNode(isButton() and hasText(activity.getString(R.string.reset))).assertIsNotDisplayed()
        composeTestRule.onNode(isButton() and hasText(activity.getString(R.string.cancel))).assertIsNotDisplayed()
    }

    fun resetDialogConfirmButton_click() {
        composeTestRule.onNode(isButton() and hasText(activity.getString(R.string.reset))).performClick()
    }
}