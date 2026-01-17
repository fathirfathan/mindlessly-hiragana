package com.effatheresoft.mindlesslyhiragana.testquiz

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertIsEnabled
import androidx.compose.ui.test.assertIsNotDisplayed
import androidx.compose.ui.test.assertIsNotEnabled
import androidx.compose.ui.test.hasScrollAction
import androidx.compose.ui.test.hasScrollToKeyAction
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performScrollToKey
import androidx.compose.ui.test.performScrollToNode
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.effatheresoft.mindlesslyhiragana.HiltTestActivity
import com.effatheresoft.mindlesslyhiragana.R
import com.effatheresoft.mindlesslyhiragana.data.model.Hiragana
import com.effatheresoft.mindlesslyhiragana.data.model.HiraganaCategory
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
class TestQuizScreenRoboTest {
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
    fun `user select incorrect answer scenario`() = runTest {
        // given user progress is `himikase`
        // and user learning sets count is `1`
        // when user select `A` as answer for `ひ` question
        // then user sees `A` button disabled
        // and user sees `ひ` question unchanged
        // and user sees remaining questions unchanged
        setContentAndNavigateToTestQuizScreen()
        hiraganaKeyboard_clickAnswer(Hiragana.A)

        composeTestRule.onNodeWithText("A").assertIsDisplayed()
        composeTestRule.onNodeWithText("A").assertIsNotEnabled()
        composeTestRule.onNodeWithText("ひ").assertIsDisplayed()
        composeTestRule.onNodeWithText(activity.getString(R.string.remaining_n, 3)).assertIsDisplayed()
    }

    @Test
    fun `user select correct answer scenario`() = runTest {
        // given user progress is `himikase`
        // and user learning sets count is `1`
        // when user select `HI` as answer for `ひ` question
        // then user sees current question changed to `み`
        // and user sees remaining questions changed to `2`
        // and user sees `HI` button not disabled
        setContentAndNavigateToTestQuizScreen()
        hiraganaKeyboard_clickAnswer(Hiragana.HI)

        composeTestRule.onNodeWithText("HI").assertIsDisplayed()
        composeTestRule.onNodeWithText("HI").assertIsEnabled()
        composeTestRule.onNodeWithText("み").assertIsDisplayed()
        composeTestRule.onNodeWithText(activity.getString(R.string.remaining_n, 2)).assertIsDisplayed()
    }

    @Test
    fun `user select all correct answers scenario`() = runTest {
        // given user progress is `himikase`
        // and user learning sets count is `1`
        // when user select all correct answers
        // then user navigates to test result screen
        // and user sees correct answers count is `4`
        // and user sees incorrect answers count is `0`
        // and user sees `Test All Learned` button enabled
        // and user sees no incorrect hiragana
        setContentAndNavigateToTestQuizScreen()
        hiraganaKeyboard_clickAnswer(Hiragana.HI)
        hiraganaKeyboard_clickAnswer(Hiragana.MI)
        hiraganaKeyboard_clickAnswer(Hiragana.KA)
        hiraganaKeyboard_clickAnswer(Hiragana.SE)

        composeTestRule.onNodeWithText(activity.getString(R.string.correct_n, 4)).assertIsDisplayed()
        composeTestRule.onNodeWithText(activity.getString(R.string.incorrect_n, 0)).assertIsDisplayed()
        composeTestRule.onNodeWithText(activity.getString(R.string.try_again)).assertIsDisplayed()
        composeTestRule.onNodeWithText(activity.getString(R.string.try_again)).assertIsNotEnabled()
        composeTestRule.onNodeWithText(activity.getString(R.string.continue_learning)).assertIsDisplayed()
        composeTestRule.onNodeWithText(activity.getString(R.string.continue_learning)).assertIsEnabled()

        listOf("ひ(HI)", "み(MI)", "か(KA)", "せ(SE)").forEach {
            composeTestRule.onNodeWithText(it).assertIsNotDisplayed()
        }
    }

    @Test
    fun `user select all correct answers except one scenario`() = runTest {
        // given user progress is `himikase`
        // and user learning sets count is `1`
        // when user click all correct buttons except `MI`
        // then user navigates to test result screen
        // and user sees correct answers count is `3`
        // and user sees incorrect answers count is `1`
        // and user sees `Test All Learned` button disabled
        // and user sees `MI` as incorrect hiragana
        setContentAndNavigateToTestQuizScreen()
        hiraganaKeyboard_clickAnswer(Hiragana.HI)
        hiraganaKeyboard_clickAnswer(Hiragana.HI)
        hiraganaKeyboard_clickAnswer(Hiragana.MI)
        hiraganaKeyboard_clickAnswer(Hiragana.KA)
        hiraganaKeyboard_clickAnswer(Hiragana.SE)

        composeTestRule.onNodeWithText(activity.getString(R.string.correct_n, 3)).assertIsDisplayed()
        composeTestRule.onNodeWithText(activity.getString(R.string.incorrect_n, 1)).assertIsDisplayed()
        composeTestRule.onNodeWithText(activity.getString(R.string.try_again)).assertIsDisplayed()
        composeTestRule.onNodeWithText(activity.getString(R.string.try_again)).assertIsEnabled()
        composeTestRule.onNodeWithText(activity.getString(R.string.continue_learning)).assertIsDisplayed()
        composeTestRule.onNodeWithText(activity.getString(R.string.continue_learning)).assertIsNotEnabled()

        composeTestRule.onNodeWithText("み(MI)").assertIsDisplayed()
        listOf("ひ(HI)", "か(KA)", "せ(SE)").forEach {
            composeTestRule.onNodeWithText(it).assertIsNotDisplayed()
        }
    }

    @Test
    fun `user correctly answers last category scenario`() = runTest {
        // given user progress is `sakichira`
        // when user click all correct buttons
        // then user navigates to test result screen
        // and user sees correct answers count is `46`
        // and user sees incorrect answers count is `0`
        // and user don't see any incorrect hiragana
        // and user sees `Try Again` button enabled
        // and user sees `Continue Learning` button disabled
        userRepository.updateLocalUserProgress(HiraganaCategory.SAKICHIRA)
        setContentAndNavigateToTestQuizScreen()
        HiraganaCategory.SAKICHIRA.complementedHiraganaList.forEach {
            hiraganaKeyboard_clickAnswer(it)
        }

        composeTestRule.onNodeWithText(activity.getString(R.string.correct_n, 46)).assertIsDisplayed()
        composeTestRule.onNodeWithText(activity.getString(R.string.incorrect_n, 0)).assertIsDisplayed()
        composeTestRule.onNodeWithText(activity.getString(R.string.try_again)).assertIsDisplayed()
        composeTestRule.onNodeWithText(activity.getString(R.string.try_again)).assertIsEnabled()
        composeTestRule.onNodeWithText(activity.getString(R.string.continue_learning)).assertIsDisplayed()
        composeTestRule.onNodeWithText(activity.getString(R.string.continue_learning)).assertIsNotEnabled()

        HiraganaCategory.SAKICHIRA.complementedHiraganaList.forEach {
            composeTestRule.onNodeWithText(it.kana, substring = true).assertIsNotDisplayed()
        }
    }

    fun hiraganaKeyboard_clickAnswer(answer: Hiragana) {
        composeTestRule.onNode(hasScrollToKeyAction()).performScrollToKey(answer)
        composeTestRule.onNodeWithText(answer.name).performClick()
    }

    suspend fun setContentAndNavigateToTestQuizScreen() {
        userRepository.updateLocalUserIsTestUnlocked(true)
        userRepository.updateLocalUserLearningSetsCount(1)
        composeTestRule.setContent {
            MindlesslyHiraganaTheme {
                DefaultNavGraph()
            }
        }
        composeTestRule.onNode(hasScrollAction()).performScrollToNode(hasText(activity.getString(R.string.test_all_learned)))
        composeTestRule.onNodeWithText(activity.getString(R.string.test_all_learned)).performClick()
        composeTestRule.onNode(isButton() and hasText(activity.getString(R.string.test_all_learned))).performClick()
    }
}