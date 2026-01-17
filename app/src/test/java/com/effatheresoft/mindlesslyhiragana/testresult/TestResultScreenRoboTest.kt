package com.effatheresoft.mindlesslyhiragana.testresult

import androidx.compose.ui.semantics.ProgressBarRangeInfo
import androidx.compose.ui.test.assertAll
import androidx.compose.ui.test.assertCountEquals
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.hasProgressBarRangeInfo
import androidx.compose.ui.test.hasScrollToKeyAction
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onAllNodesWithText
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
        // and `learn button` is displayed
        setContentAndNavigateToTestResultScreen()
        composeTestRule.onNodeWithText(activity.getString(R.string.continue_learning)).performClick()

        with(composeTestRule.onAllNodesWithText(activity.getString(R.string.learn))) {
            assertCountEquals(2)
            assertAll(hasText(activity.getString(R.string.learn)))
        }
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
        composeTestRule.onNode(isButton() and hasText(activity.getString(R.string.learn))).assertIsDisplayed()
    }

    fun hiraganaKeyboard_clickAnswer(answer: Hiragana) {
        composeTestRule.onNode(hasScrollToKeyAction()).performScrollToKey(answer)
        composeTestRule.onNodeWithText(answer.name).performClick()
    }

    suspend fun setContentAndNavigateToTestResultScreen() {
        userRepository.updateLocalUserIsTestUnlocked(true)
        userRepository.updateLocalUserLearningSetsCount(1)
        composeTestRule.setContent {
            MindlesslyHiraganaTheme {
                DefaultNavGraph()
            }
        }
        composeTestRule.onNodeWithText(activity.getString(R.string.test_all_learned)).performClick()
        composeTestRule.onNode(isButton() and hasText(activity.getString(R.string.test_all_learned))).performClick()
        listOf(Hiragana.HI, Hiragana.MI, Hiragana.KA, Hiragana.SE).forEach {
            hiraganaKeyboard_clickAnswer(it)
        }
    }
}