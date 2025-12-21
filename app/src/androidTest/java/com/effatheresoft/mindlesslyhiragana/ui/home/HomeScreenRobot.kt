package com.effatheresoft.mindlesslyhiragana.ui.home

import androidx.activity.ComponentActivity
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.junit4.AndroidComposeTestRule
import androidx.compose.ui.test.onAllNodesWithText
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import com.effatheresoft.mindlesslyhiragana.data.model.HiraganaCategory
import com.effatheresoft.mindlesslyhiragana.data.repository.UserRepository
import com.effatheresoft.mindlesslyhiragana.ui.learn.isButton
import org.junit.rules.TestRule

class HomeScreenRobot <R : TestRule, A : ComponentActivity> (
    private val composeTestRule: AndroidComposeTestRule<R, A>,
    private val userRepository: UserRepository
) {
    val activity get() = composeTestRule.activity

    suspend fun setLocalUserProgress(progress: String) {
        userRepository.updateLocalUserProgress(progress)
    }

    fun clickCategory(category: HiraganaCategory) {
        composeTestRule.onNodeWithText(category.kanaWithNakaguro).performClick()
    }

    fun assert_onHomeScreen() {
        composeTestRule.onAllNodesWithText(activity.getString(com.effatheresoft.mindlesslyhiragana.R.string.mindlessly_hiragana))[0].assertIsDisplayed()
    }

    fun clickTestAllLearnedButton() {
        composeTestRule.onNode(isButton() and hasText(activity.getString(com.effatheresoft.mindlesslyhiragana.R.string.test_all_learned))).performClick()
    }

    fun topAppBarMenu_assertIsDisplayed() {
        composeTestRule.onNodeWithContentDescription(activity.getString(com.effatheresoft.mindlesslyhiragana.R.string.open_menu)).assertIsDisplayed()
    }

    fun topAppBarMenu_click() {
        composeTestRule.onNodeWithContentDescription(activity.getString(com.effatheresoft.mindlesslyhiragana.R.string.open_menu)).performClick()
    }

    fun drawer_assertIsDisplayed() {
        composeTestRule.onNodeWithText(activity.getString(com.effatheresoft.mindlesslyhiragana.R.string.reset_progress)).assertIsDisplayed()
    }

    fun drawerTitleText_assertIsDisplayed() {
        composeTestRule.onAllNodesWithText(activity.getString(com.effatheresoft.mindlesslyhiragana.R.string.mindlessly_hiragana))[1].assertIsDisplayed()
    }

    fun drawerResetProgressButton_assertIsDisplayed() {
        composeTestRule.onNodeWithText(activity.getString(com.effatheresoft.mindlesslyhiragana.R.string.reset_progress)).assertIsDisplayed()
    }
}