package com.effatheresoft.mindlesslyhiragana.learn

import androidx.compose.ui.semantics.ProgressBarRangeInfo
import androidx.compose.ui.semantics.SemanticsActions
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.hasProgressBarRangeInfo
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performSemanticsAction
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.effatheresoft.mindlesslyhiragana.HiltTestActivity
import com.effatheresoft.mindlesslyhiragana.R
import com.effatheresoft.mindlesslyhiragana.navigation.DefaultNavGraph
import com.effatheresoft.mindlesslyhiragana.sharedtest.util.isButton
import com.effatheresoft.mindlesslyhiragana.ui.theme.MindlesslyHiraganaTheme
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import dagger.hilt.android.testing.HiltTestApplication
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.annotation.Config

@RunWith(AndroidJUnit4::class)
@HiltAndroidTest
@Config(application = HiltTestApplication::class)
class LearnScreenRoboTest {
    @get:Rule(order = 0)
    var hiltRule = HiltAndroidRule(this)

    @get:Rule(order = 1)
    val composeTestRule = createAndroidComposeRule<HiltTestActivity>()
    val activity get() = composeTestRule.activity

    @Test
    fun `user change learning sets count scenario`() {
        // given user progress is `himikase`
        // and selected category is `himikase`
        // when user change learning sets count to 6
        // then user learning sets count becomes 6
        setContentAndNavigateToLearn()
        composeTestRule.onNode(
            hasProgressBarRangeInfo(
                ProgressBarRangeInfo(
                    current = 5f,
                    range = 1f..10f,
                    steps = 8
                )
            )
        ).performSemanticsAction(SemanticsActions.SetProgress) { it(6f) }
        composeTestRule.onNodeWithText(activity.getString(R.string.learning_sets_n_sets, 6)).assertIsDisplayed()
    }

    @Test
    fun `user click learn button scenario`() {
        // given user progress is `himikase`
        // and selected category is `himikase`
        // and learning sets count is 5
        // when user click learn button
        // then user navigates to quiz screen
        val learningSetsCount = 5
        setContentAndNavigateToLearn()
        composeTestRule.onNode(isButton() and hasText(activity.getString(R.string.learn))).performClick()

        composeTestRule.onNodeWithText(activity.getString(R.string.learn)).assertIsDisplayed()

        composeTestRule.onNode(
            hasText("ひ") or
                    hasText("み") or
                    hasText("か") or
                    hasText("せ")
        ).assertIsDisplayed()

        val answerButtonTexts = listOf("HI", "MI", "KA", "SE")
        composeTestRule.onNodeWithText(activity.getString(R.string.remaining_n, (answerButtonTexts.size * learningSetsCount) - 1)).assertIsDisplayed()

        answerButtonTexts.forEach {
            composeTestRule.onNodeWithText(it).assertIsDisplayed()
        }
    }

    fun setContentAndNavigateToLearn() {
        composeTestRule.setContent {
            MindlesslyHiraganaTheme {
                DefaultNavGraph()
            }
        }
        composeTestRule.onNodeWithText("ひみかせ").performClick()
    }
}