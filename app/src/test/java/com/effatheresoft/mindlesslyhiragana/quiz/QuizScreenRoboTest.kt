package com.effatheresoft.mindlesslyhiragana.quiz

import androidx.compose.ui.semantics.ProgressBarRangeInfo
import androidx.compose.ui.semantics.SemanticsActions
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertIsNotEnabled
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
class QuizScreenRoboTest {
    @get:Rule(order = 0)
    var hiltRule = HiltAndroidRule(this)

    @get:Rule(order = 1)
    val composeTestRule = createAndroidComposeRule<HiltTestActivity>()
    val activity get() = composeTestRule.activity

    @Test
    fun `user select incorrect answer scenario`() {
        // given user progress is `himikase`
        // and selected category is `himikase`
        // and learning sets count is 1
        // and shown quizzes are ひ, み, か, せ in order
        // when user click incorrect answer button
        // then user sees incorrect answer button disabled
        setContentAndNavigateToQuiz()
        composeTestRule.onNodeWithText("MI").performClick()
        composeTestRule.onNodeWithText("MI").assertIsNotEnabled()
    }

    @Test
    fun `user select correct answer scenario`() {
        // given user progress is `himikase`
        // and selected category is `himikase`
        // and learning sets count is 1
        // and shown quizzes are ひ, み, か, せ in order
        // when user click correct answer button
        // then user sees next quiz question
        setContentAndNavigateToQuiz()
        composeTestRule.onNodeWithText("HI").performClick()
        composeTestRule.onNodeWithText("み").assertIsDisplayed()
    }

    fun setContentAndNavigateToQuiz() {
        composeTestRule.setContent {
            MindlesslyHiraganaTheme {
                DefaultNavGraph()
            }
        }
        composeTestRule.onNodeWithText("ひみかせ").performClick()
        composeTestRule.onNode(
            hasProgressBarRangeInfo(
                ProgressBarRangeInfo(
                    current = 5f,
                    range = 1f..10f,
                    steps = 8
                )
            )
        ).performSemanticsAction(SemanticsActions.SetProgress) { it(1f) }
        composeTestRule.onNode(isButton() and hasText(activity.getString(R.string.learn))).performClick()
    }
}