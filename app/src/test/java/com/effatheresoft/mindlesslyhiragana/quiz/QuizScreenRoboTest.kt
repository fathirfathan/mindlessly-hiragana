package com.effatheresoft.mindlesslyhiragana.quiz

import androidx.compose.ui.semantics.ProgressBarRangeInfo
import androidx.compose.ui.semantics.SemanticsActions
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertIsEnabled
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
import com.effatheresoft.mindlesslyhiragana.data.repository.RefactoredQuizRepository
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
class QuizScreenRoboTest {
    @get:Rule(order = 0)
    var hiltRule = HiltAndroidRule(this)

    @get:Rule(order = 1)
    val composeTestRule = createAndroidComposeRule<HiltTestActivity>()
    val activity get() = composeTestRule.activity

    @Inject lateinit var userRepository: RefactoredUserRepository
    @Inject lateinit var quizRepository: RefactoredQuizRepository

    @Before
    fun init() {
        hiltRule.inject()
    }

    @Test
    fun `user select incorrect answer scenario`() = runTest {
        // given user progress is `himikase`
        // and selected category is `ひみかせ`
        // and learning sets count is 1
        // and shown quizzes are `ひみかせ` category in order
        // when user select `MI` answer button
        // then user sees `MI` answer button disabled
        setContentAndNavigateToQuiz("ひみかせ", 1)
        composeTestRule.onNodeWithText("MI").performClick()
        composeTestRule.onNodeWithText("MI").assertIsNotEnabled()
    }

    @Test
    fun `user select correct answer scenario`() = runTest {
        // given user progress is `himikase`
        // and selected category is `ひみかせ`
        // and learning sets count is 1
        // and shown quizzes are `ひみかせ` category in order
        // when user select `HI` answer button
        // then user sees `み` quiz question
        setContentAndNavigateToQuiz("ひみかせ", 1)
        composeTestRule.onNodeWithText("HI").performClick()
        composeTestRule.onNodeWithText("み").assertIsDisplayed()
    }

    @Test
    fun `user select all correct answer scenario`() = runTest {
        // given user progress is `himikase`
        // and selected category is `ひみかせ`
        // and learning sets count is 2
        // and shown quizzes are `ひみかせ` category in order twice
        // when user select all correct answer buttons
        // then user navigates to result screen
        setContentAndNavigateToQuiz("ひみかせ", 2)
        repeat(2) {
            listOf("HI", "MI", "KA", "SE").forEach {
                composeTestRule.onNodeWithText(it).performClick()
            }
        }

        composeTestRule.onNodeWithText(activity.getString(R.string.result)).assertIsDisplayed()
        composeTestRule.onNodeWithText(activity.getString(R.string.correct_n, 8)).assertIsDisplayed()
        composeTestRule.onNodeWithText(activity.getString(R.string.incorrect_n, 0)).assertIsDisplayed()
        composeTestRule.onNodeWithText(activity.getString(R.string.incorrect_counts)).assertIsDisplayed()
        composeTestRule.onNodeWithText("ひ: 0").assertIsDisplayed()
        composeTestRule.onNodeWithText("み: 0").assertIsDisplayed()
        composeTestRule.onNodeWithText("か: 0").assertIsDisplayed()
        composeTestRule.onNodeWithText("せ: 0").assertIsDisplayed()
        composeTestRule.onNodeWithText(activity.getString(R.string.try_again)).assertIsDisplayed()
        composeTestRule.onNodeWithText(activity.getString(R.string.test_all_learned)).assertIsDisplayed()
        composeTestRule.onNodeWithText(activity.getString(R.string.test_all_learned)).assertIsEnabled()
    }

    fun setContentAndNavigateToQuiz(category: String, learningSetsCount: Int) {
        composeTestRule.setContent {
            MindlesslyHiraganaTheme {
                DefaultNavGraph()
            }
        }
        composeTestRule.onNodeWithText(category).performClick()
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
    }
}