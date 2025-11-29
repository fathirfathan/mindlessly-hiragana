package com.effatheresoft.mindlesslyhiragana.navigation

import androidx.compose.ui.semantics.ProgressBarRangeInfo
import androidx.compose.ui.semantics.SemanticsActions
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.hasProgressBarRangeInfo
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performSemanticsAction
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.effatheresoft.mindlesslyhiragana.HiltTestActivity
import com.effatheresoft.mindlesslyhiragana.ui.theme.MindlesslyHiraganaTheme
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
@HiltAndroidTest
class DefaultNavGraphTest {

    @get:Rule(order = 0)
    var hiltRule = HiltAndroidRule(this)

    @get:Rule(order = 1)
    val composeTestRule = createAndroidComposeRule<HiltTestActivity>()
    private val activity get() = composeTestRule.activity

    @Before
    fun init() {
        hiltRule.inject()
    }

    @Test
    fun startDestination_isHomeScreen() {
        setContent()

        composeTestRule.onNodeWithText("Mindlessly Hiragana").assertIsDisplayed()
    }

    @Test
    fun homeScreen_onCategoryClicked_navigatesToLearnScreen() {
        setContent()

        composeTestRule.onNodeWithText("ひみかせ").performClick()
        composeTestRule.onNodeWithText("Learning Sets", substring = true).assertIsDisplayed()
        composeTestRule.onNodeWithText("ひみかせ").assertIsDisplayed()
    }

    @Test
    fun learnScreen_onNavigationIconClicked_navigatesToHomeScreen() {
        setContent(Route.Learn("himikase"))

        composeTestRule.onNodeWithContentDescription("Navigate back").performClick()
        composeTestRule.onNodeWithText("Mindlessly Hiragana").assertIsDisplayed()
    }

    @Test
    fun whenLearningSetsCountChanged_countStaysChanged() {
        val currentCount = 5
        val changedCount = 1
        setContent(Route.Learn("himikase"))

        // LearnScreen()
        composeTestRule.onNode(
            hasProgressBarRangeInfo(
                ProgressBarRangeInfo(
                    current = currentCount.toFloat(),
                    range = 1f..10f,
                    steps = 8
                )
            )
        ).performSemanticsAction(SemanticsActions.SetProgress) { it(changedCount.toFloat()) }
        composeTestRule.onNodeWithContentDescription("Navigate back").performClick()

        // HomeScreen()
        composeTestRule.onNodeWithText("ひみかせ").performClick()

        // LearnScreen()
        composeTestRule.onNode(
            hasProgressBarRangeInfo(
                ProgressBarRangeInfo(
                    current = changedCount.toFloat(),
                    range = 1f..10f,
                    steps = 8
                )
            )
        ).assertIsDisplayed()
        composeTestRule.onNodeWithText("Learning Sets: $changedCount Sets").assertIsDisplayed()
    }

    fun setContent(startDestination: Route = Route.Home) {
        composeTestRule.setContent {
            MindlesslyHiraganaTheme {
                DefaultNavGraph(startDestination = startDestination)
            }
        }
    }
}