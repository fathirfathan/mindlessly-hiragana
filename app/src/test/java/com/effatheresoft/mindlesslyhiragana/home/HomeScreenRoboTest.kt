package com.effatheresoft.mindlesslyhiragana.home

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertIsNotDisplayed
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.onParent
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performScrollToNode
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.effatheresoft.mindlesslyhiragana.HiltTestActivity
import com.effatheresoft.mindlesslyhiragana.R
import com.effatheresoft.mindlesslyhiragana.navigation.DefaultNavGraph
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
class HomeScreenRoboTest {
    @get:Rule(order = 0)
    var hiltRule = HiltAndroidRule(this)

    @get:Rule(order = 1)
    val composeTestRule = createAndroidComposeRule<HiltTestActivity>()
    val activity get() = composeTestRule.activity

    @Test
    fun `user open home screen for the first time scenario`() {
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
    fun `user click unlocked category scenario`() {
        // given user progress is `himikase`
        // when user click unlocked category `himikase`
        // then user navigates to learn screen
        setContent()
        composeTestRule.onNodeWithText("ひみかせ").performClick()
        composeTestRule.onNodeWithText("Learning Sets", substring = true).assertIsDisplayed()
        composeTestRule.onNodeWithText("ひみかせ").assertIsDisplayed()
    }

    @Test
    fun `user click locked category scenario`() {
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
    fun `user click test category scenario`() {
        // given user progress is `himikase`
        // when user click Test All Learned category
        // then user navigates to test screen
        setContent()
        composeTestRule.onNodeWithText(activity.getString(R.string.test_all_learned)).performClick()
        composeTestRule.onNodeWithText(activity.getString(R.string.including)).assertIsDisplayed()
        composeTestRule.onNodeWithText("ひみかせ").assertIsDisplayed()
        composeTestRule.onNodeWithText("ふをや").assertIsNotDisplayed()
    }

    fun setContent() {
        composeTestRule.setContent {
            MindlesslyHiraganaTheme {
                DefaultNavGraph()
            }
        }
    }
}