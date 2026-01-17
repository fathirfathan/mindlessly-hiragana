package com.effatheresoft.mindlesslyhiragana.testresult

import androidx.compose.ui.semantics.ProgressBarRangeInfo
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertIsEnabled
import androidx.compose.ui.test.assertIsNotDisplayed
import androidx.compose.ui.test.hasProgressBarRangeInfo
import androidx.compose.ui.test.hasScrollToKeyAction
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performScrollToKey
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.effatheresoft.mindlesslyhiragana.HiltTestActivity
import com.effatheresoft.mindlesslyhiragana.R
import com.effatheresoft.mindlesslyhiragana.data.model.Hiragana
import com.effatheresoft.mindlesslyhiragana.data.repository.RefactoredUserRepository
import com.effatheresoft.mindlesslyhiragana.navigation.DefaultNavGraph
import com.effatheresoft.mindlesslyhiragana.sharedtest.util.isButton
import com.effatheresoft.mindlesslyhiragana.ui.theme.MindlesslyHiraganaTheme
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import dagger.hilt.android.testing.HiltTestApplication
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.annotation.Config
import javax.inject.Inject

@RunWith(AndroidJUnit4::class)
@HiltAndroidTest
@Config(application = HiltTestApplication::class)
class TestResultScreenRoboTest {
    @get:Rule(order = 0)
    var hiltRule = HiltAndroidRule(this)

    @get:Rule(order = 1)
    val composeTestRule = createAndroidComposeRule<HiltTestActivity>()
    val activity get() = composeTestRule.activity

    @Inject lateinit var userRepository: RefactoredUserRepository

    @Before
    fun init() {
        hiltRule.inject()
    }

    @Test
    fun `user click continue learning scenario`() = runTest {
        // given user progress is `himikase`
        // and user learning sets count is `1`
        // and user select all correct answers
        // when user click `Continue Learning` button
        // then user navigates to learn screen
        // and user sees `ふをや` category
        // and learning sets count is `1`
        setContentAndNavigateToTestResultScreen(isAllCorrect = true)
        composeTestRule.onNodeWithText(activity.getString(R.string.continue_learning)).performClick()

        composeTestRule.onNodeWithText("ふをや").assertIsDisplayed()
        composeTestRule.onNodeWithText(activity.getString(R.string.learning_sets_n_sets, 1)).assertIsDisplayed()
        composeTestRule.onNode(
            hasProgressBarRangeInfo(
                ProgressBarRangeInfo(
                    current = 1f,
                    range = 1f..10f,
                    steps = 8
                )
            )
        ).assertIsDisplayed()
    }

    @Test
    fun `user click try again scenario`() = runTest {
        // given user progress is `himikase`
        // and user learning sets count is `1`
        // and user select all correct answers except one
        // when user click `Try Again` button
        // then user navigates to test screen
        // and user sees only `ひみかせ` category
        // and `Test All Learned` button is enabled
        setContentAndNavigateToTestResultScreen(isAllCorrect = false)
        composeTestRule.onNodeWithText(activity.getString(R.string.try_again)).performClick()

        composeTestRule.onNodeWithText(activity.getString(R.string.test_categories_n, 1))
        composeTestRule.onNodeWithText("ひみかせ").assertIsDisplayed()
        composeTestRule.onNodeWithText("ふをや").assertIsNotDisplayed()
        composeTestRule.onNode(isButton() and hasText(activity.getString(R.string.test_all_learned))).assertIsDisplayed()
        composeTestRule.onNode(isButton() and hasText(activity.getString(R.string.test_all_learned))).assertIsEnabled()
    }

    fun hiraganaKeyboard_clickAnswer(answer: Hiragana) {
        composeTestRule.onNode(hasScrollToKeyAction()).performScrollToKey(answer)
        composeTestRule.onNodeWithText(answer.name).performClick()
    }

    suspend fun setContentAndNavigateToTestResultScreen(isAllCorrect: Boolean) {
        userRepository.updateLocalUserIsTestUnlocked(true)
        userRepository.updateLocalUserLearningSetsCount(1)
        composeTestRule.setContent {
            MindlesslyHiraganaTheme {
                DefaultNavGraph()
            }
        }
        composeTestRule.onNodeWithText(activity.getString(R.string.test_all_learned)).performClick()
        composeTestRule.onNode(isButton() and hasText(activity.getString(R.string.test_all_learned))).performClick()
        when(isAllCorrect) {
            true -> listOf(Hiragana.HI)
            false -> listOf(Hiragana.HI, Hiragana.HI)
        }.plus(listOf(Hiragana.MI, Hiragana.KA, Hiragana.SE)).forEach {
            hiraganaKeyboard_clickAnswer(it)
        }
    }
}