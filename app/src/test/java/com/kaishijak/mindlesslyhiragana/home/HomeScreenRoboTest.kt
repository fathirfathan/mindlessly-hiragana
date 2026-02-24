package com.kaishijak.mindlesslyhiragana.home

import androidx.compose.ui.test.assertAll
import androidx.compose.ui.test.assertCountEquals
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertIsEnabled
import androidx.compose.ui.test.assertIsNotDisplayed
import androidx.compose.ui.test.assertIsNotEnabled
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onAllNodesWithText
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.onParent
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performScrollToNode
import androidx.compose.ui.test.performTouchInput
import androidx.compose.ui.test.swipeLeft
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.kaishijak.mindlesslyhiragana.HiltTestActivity
import com.kaishijak.mindlesslyhiragana.R
import com.kaishijak.mindlesslyhiragana.data.model.HiraganaCategory
import com.kaishijak.mindlesslyhiragana.data.repository.UserRepository
import com.kaishijak.mindlesslyhiragana.navigation.DefaultNavGraph
import com.kaishijak.mindlesslyhiragana.sharedtest.util.isButton
import com.kaishijak.mindlesslyhiragana.ui.theme.MindlesslyHiraganaTheme
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
class HomeScreenRoboTest {
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
    fun `user open home screen for the first time scenario`() = runTest {
        // given user progress is `himikase`
        // when user open home screen
        // then `himikase` category is unlocked
        // and `Test All Learned` category is
        // and categories after `Test All Learned` are locked
        setContent()
        val categoryTitles = listOf("ひみかせ", "Test All Learned", "ふをや", "あお", "つう・んえ", "くへ・りけ", "こに・たな", "すむ・ろる", "しいも", "とてそ", "わねれ", "のゆめぬ", "よはまほ", "さきちら")
        for (title in categoryTitles) {
            composeTestRule.onNodeWithText(title).onParent().performScrollToNode(hasText(title))
            composeTestRule.onNodeWithText(title).assertIsDisplayed()

            when (title) {
                "ひみかせ" -> composeTestRule.onNodeWithContentDescription(activity.getString(R.string.x_category_unlocked, title)).assertIsDisplayed()
                "Test All Learned" -> composeTestRule.onNodeWithContentDescription(activity.getString(R.string.x_category_unlocked, "Test All Learned")).assertIsDisplayed()
                else -> composeTestRule.onNodeWithContentDescription(activity.getString(R.string.x_category_locked, title)).assertIsDisplayed()
            }
        }
    }

    @Test
    fun `user click unlocked category scenario`() = runTest {
        // given user progress is `himikase`
        // when user click unlocked category `himikase`
        // then user navigates to learn screen
        setContent()

        composeTestRule.onNodeWithText("ひみかせ").performClick()
        composeTestRule.onNodeWithText("Learning Sets", substring = true).assertIsDisplayed()
        composeTestRule.onNodeWithText("ひみかせ").assertIsDisplayed()
    }

    @Test
    fun `user click unlocked category different from progress scenario`() = runTest {
        // given user progress is `ao`
        // when user click unlocked category `fuwoya`
        // then user navigates to learn screen
        userRepository.updateHighestCategory(HiraganaCategory.AO)
        setContent()

        composeTestRule.onNodeWithText("ふをや").performClick()
        composeTestRule.onNodeWithText("Learning Sets", substring = true).assertIsDisplayed()
        composeTestRule.onNodeWithText("ふをや").assertIsDisplayed()
    }

    @Test
    fun `user click locked category scenario`() = runTest {
        // given user progress is `himikase`
        // when user click locked category `fuwoya`
        // then nothing happens
        setContent()

        composeTestRule.onNodeWithText("ふをや").performClick()
        composeTestRule.onNodeWithText("ひみかせ").assertIsDisplayed()
        composeTestRule.onNodeWithText("ふをや").assertIsDisplayed()
        composeTestRule.onNodeWithText("あお").assertIsDisplayed()
    }

    @Test
    fun `user click test category scenario`() = runTest {
        // given user progress is `himikase`
        // and user isTestUnlocked is false
        // when user click Test All Learned category
        // then user navigates to test screen
        setContent()
        composeTestRule.onNodeWithText(activity.getString(R.string.test_all_learned)).performClick()

        with(composeTestRule.onAllNodesWithText(activity.getString(R.string.test_all_learned))) {
            assertCountEquals(2)
            assertAll(hasText(activity.getString(R.string.test_all_learned)))
        }
        composeTestRule.onNodeWithText(activity.getString(R.string.test_categories_n, 1)).assertIsDisplayed()
        composeTestRule.onNodeWithText(activity.getString(R.string.including)).assertIsDisplayed()
        composeTestRule.onNodeWithText("ひみかせ").assertIsDisplayed()
        composeTestRule.onNodeWithText("ふをや").assertIsNotDisplayed()
        composeTestRule.onNodeWithText(activity.getString(R.string.challenge_all_correct_on_learn)).assertIsDisplayed()
        with(composeTestRule.onNode(isButton() and hasText(activity.getString(R.string.test_all_learned)))) {
            assertIsDisplayed()
            assertIsNotEnabled()
        }
    }

    @Test
    fun `user click test category and test is unlocked scenario`() = runTest {
        // given user progress is `himikase`
        // and user isTestUnlocked is true
        // when user click Test All Learned category
        // then user sees `Test All Learned` button enabled
        userRepository.updateIsTestUnlocked(true)
        setContent()
        composeTestRule.onNodeWithText(activity.getString(R.string.test_all_learned)).performClick()

        with(composeTestRule.onNode(isButton() and hasText(activity.getString(R.string.test_all_learned)))) {
            assertIsDisplayed()
            assertIsEnabled()
        }
    }

    @Test
    fun `user open reset progress dialog scenario`() = runTest {
        // given user progress is `himikase`
        // when user click top app bar menu icon
        // and user click reset progress button
        // then user sees reset progress dialog
        setContent()

        composeTestRule.onNodeWithContentDescription(activity.getString(R.string.open_menu)).performClick()
        composeTestRule.onNodeWithText(activity.getString(R.string.reset_progress)).assertIsDisplayed()
        composeTestRule.onNodeWithText(activity.getString(R.string.reset_progress)).performClick()
        composeTestRule.onNodeWithText(activity.getString(R.string.reset)).assertIsDisplayed()
    }

    @Test
    fun `user reset progress scenario`() = runTest {
        // given user progress is `fuwoya`
        // when user click top app bar navigation icon
        // and user click reset progress button
        // and user click reset button
        // then user sees `ふをや` category locked
        userRepository.updateHighestCategory(HiraganaCategory.FUWOYA)
        setContent()
        composeTestRule.onNodeWithContentDescription(activity.getString(R.string.x_category_unlocked, "ふをや")).assertIsDisplayed()
        composeTestRule.onNodeWithContentDescription(activity.getString(R.string.open_menu)).performClick()
        composeTestRule.onNodeWithText(activity.getString(R.string.reset_progress)).performClick()
        composeTestRule.onNode(isButton() and hasText(activity.getString(R.string.reset))).performClick()

        composeTestRule.onNodeWithContentDescription(activity.getString(R.string.x_category_locked, "ふをや")).assertIsDisplayed()
    }

    @Test
    fun `user cancel reset progress dialog scenario`() = runTest {
        // given user progress is `fuwoya`
        // when user click top app bar navigation icon
        // and user click reset progress button
        // and user click cancel button
        // then user sees `ふをや` category still unlocked
        userRepository.updateHighestCategory(HiraganaCategory.FUWOYA)
        setContent()
        composeTestRule.onNodeWithContentDescription(activity.getString(R.string.x_category_unlocked, "ふをや")).assertIsDisplayed()
        composeTestRule.onNodeWithContentDescription(activity.getString(R.string.open_menu)).performClick()
        composeTestRule.onNodeWithText(activity.getString(R.string.reset_progress)).performClick()
        composeTestRule.onNode(isButton() and hasText(activity.getString(R.string.cancel))).performClick()

        composeTestRule.onNodeWithText(activity.getString(R.string.reset_progress)).performTouchInput { swipeLeft() }
        composeTestRule.onNodeWithContentDescription(activity.getString(R.string.x_category_unlocked, "ふをや")).assertIsDisplayed()
    }

    fun setContent() {
        composeTestRule.setContent {
            MindlesslyHiraganaTheme {
                DefaultNavGraph()
            }
        }
    }
}