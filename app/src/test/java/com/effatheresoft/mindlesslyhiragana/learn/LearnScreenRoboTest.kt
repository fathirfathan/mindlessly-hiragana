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
import com.effatheresoft.mindlesslyhiragana.data.repository.UserRepository
import com.effatheresoft.mindlesslyhiragana.navigation.DefaultNavGraph
import com.effatheresoft.mindlesslyhiragana.sharedtest.util.isButton
import com.effatheresoft.mindlesslyhiragana.ui.theme.MindlesslyHiraganaTheme
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import dagger.hilt.android.testing.HiltTestApplication
import jakarta.inject.Inject
import kotlinx.coroutines.test.runTest
import org.junit.Before
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

    @Inject lateinit var userRepository: UserRepository

    @Before
    fun init() {
        hiltRule.inject()
    }

    @Test
    fun `user change learning sets count scenario`() = runTest {
        // given user progress is `himikase`
        // and selected category is `ひみかせ`
        // when user change learning sets count to 6
        // then user learning sets count becomes 6
        setContentAndNavigateToLearn("ひみかせ")
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
    fun `user click learn button scenario`() = runTest {
        // given user progress is `himikase`
        // and selected category is `ひみかせ`
        // and learning sets count is 6
        // and shown quizzes are `ひみかせ` category in order
        // when user click learn button
        // then user navigates to quiz screen
        setContentAndNavigateToLearn("ひみかせ")
        val learningSetsCount = 6
        composeTestRule.onNode(
            hasProgressBarRangeInfo(
                ProgressBarRangeInfo(
                    current = 5f,
                    range = 1f..10f,
                    steps = 8
                )
            )
        ).performSemanticsAction(SemanticsActions.SetProgress) { it(learningSetsCount.toFloat()) }
        composeTestRule.onNode(isButton() and hasText(activity.getString(R.string.learn))).performClick()

        composeTestRule.onNodeWithText(activity.getString(R.string.learn)).assertIsDisplayed()
        composeTestRule.onNodeWithText("ひ").assertIsDisplayed()

        val answerButtonTexts = listOf("HI", "MI", "KA", "SE")
        composeTestRule.onNodeWithText(activity.getString(R.string.remaining_n, (answerButtonTexts.size * learningSetsCount) - 1)).assertIsDisplayed()

        answerButtonTexts.forEach {
            composeTestRule.onNodeWithText(it).assertIsDisplayed()
        }
    }

    fun setContentAndNavigateToLearn(categoryTitle: String) {
        composeTestRule.setContent {
            MindlesslyHiraganaTheme {
                DefaultNavGraph()
            }
        }
        composeTestRule.onNodeWithText(categoryTitle).performClick()
    }
}