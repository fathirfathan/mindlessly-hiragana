package com.effatheresoft.mindlesslyhiragana.ui.test

import androidx.activity.ComponentActivity
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertIsEnabled
import androidx.compose.ui.test.assertIsNotEnabled
import androidx.compose.ui.test.hasContentDescription
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.junit4.AndroidComposeTestRule
import androidx.compose.ui.test.onAllNodesWithText
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.performClick
import com.effatheresoft.mindlesslyhiragana.R
import com.effatheresoft.mindlesslyhiragana.data.model.HiraganaCategory
import com.effatheresoft.mindlesslyhiragana.data.repository.UserRepository
import com.effatheresoft.mindlesslyhiragana.ui.learn.isButton
import org.junit.rules.TestRule
import kotlin.properties.Delegates

class TestScreenRobot <TR : TestRule, CA : ComponentActivity> (
    private val composeTestRule: AndroidComposeTestRule<TR, CA>,
    private val userRepository: UserRepository
) {

    private val activity get() = composeTestRule.activity
    private lateinit var progress: String
    lateinit var categoryList: List<HiraganaCategory>
    private var isTestUnlocked by Delegates.notNull<Boolean>()

    suspend fun setProgress(progress: String) {
        userRepository.updateLocalUserProgress(progress)
        this.progress = progress
        categoryList = HiraganaCategory.progressToCategoryList(this.progress)
    }

    suspend fun setIsTestUnlocked(value: Boolean) {
        userRepository.updateLocalUserIsTestUnlocked(value)
        isTestUnlocked = value
    }

    fun assertOnTestScreen() {
        topAppBarTitle_assertIsDisplayed()
        testButton_assertIsDisplayed()
    }

    fun click_navigateUpButton() {
        composeTestRule.onNodeWithContentDescription(activity.getString(R.string.navigate_back)).performClick()
    }

    fun topAppBarTitle_assertIsDisplayed() {
        composeTestRule.onAllNodesWithText(activity.getString(R.string.test_all_learned))[0].assertIsDisplayed()
    }

    fun topAppBarNavButton_assertIsDisplayed() {
        composeTestRule.onNode(hasContentDescription(activity.getString(R.string.navigate_back))).assertIsDisplayed()
    }

    fun topAppBarNavButton_click() {
        composeTestRule.onNode(hasContentDescription(activity.getString(R.string.navigate_back))).performClick()
    }

    fun questionsText_assertIsDisplayed(value: Int? = null) {
        composeTestRule.onNode(
            hasText(activity.getString(R.string.questions, value ?: ""), substring = value == null)
        ).assertIsDisplayed()
    }

    fun includingText_assertIsDisplayed() {
        composeTestRule.onNode(hasText(activity.getString(R.string.including))).assertIsDisplayed()
    }

    fun categoryText_assertIsDisplayed() {
        categoryList.forEach { category ->
            composeTestRule.onNode(hasText(category.kanaWithNakaguro)).assertIsDisplayed()
        }
    }

    fun challengeButton_assertIsDisplayed() {
        composeTestRule.onNode(isButton() and hasText(activity.getString(R.string.challenge_all_correct_on_learn))).assertIsDisplayed()
    }

    fun challengeButton_click() {
        composeTestRule.onNode(isButton() and hasText(activity.getString(R.string.challenge_all_correct_on_learn))).performClick()
    }

    fun testButton_assertIsDisplayed() {
        composeTestRule.onNode(isButton() and hasText(activity.getString(R.string.test_all_learned))).assertIsDisplayed()
    }

    fun testButton_assertIsEnabled() {
        composeTestRule.onNode(isButton() and hasText(activity.getString(R.string.test_all_learned))).assertIsEnabled()
    }

    fun testButton_assertIsDisabled() {
        composeTestRule.onNode(isButton() and hasText(activity.getString(R.string.test_all_learned))).assertIsNotEnabled()
    }
}