package com.effatheresoft.mindlesslyhiragana.navigation

import androidx.compose.ui.semantics.ProgressBarRangeInfo
import androidx.compose.ui.semantics.SemanticsActions
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.hasProgressBarRangeInfo
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performSemanticsAction
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.effatheresoft.mindlesslyhiragana.HiltTestActivity
import com.effatheresoft.mindlesslyhiragana.R
import com.effatheresoft.mindlesslyhiragana.data.Hiragana.HI
import com.effatheresoft.mindlesslyhiragana.data.Hiragana.KA
import com.effatheresoft.mindlesslyhiragana.data.Hiragana.MI
import com.effatheresoft.mindlesslyhiragana.data.Hiragana.SE
import com.effatheresoft.mindlesslyhiragana.data.HiraganaCategory.HIMIKASE
import com.effatheresoft.mindlesslyhiragana.learn.isButton
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

        composeTestRule.onNodeWithText(activity.getString(R.string.mindlessly_hiragana)).assertIsDisplayed()
    }

    @Test
    fun homeScreen_onCategoryClicked_navigatesToLearnScreen() {
        setContent()

        composeTestRule.onNodeWithText(HIMIKASE.kanaWithNakaguro).performClick()
        composeTestRule.onNodeWithText(activity.getString(R.string.learning_sets), substring = true).assertIsDisplayed()
        composeTestRule.onNodeWithText(HIMIKASE.kanaWithNakaguro).assertIsDisplayed()
    }

    @Test
    fun learnScreen_onNavigationIconClicked_navigatesToHomeScreen() {
        setContent(Route.Learn(HIMIKASE.id))

        composeTestRule.onNodeWithContentDescription(activity.getString(R.string.navigate_back)).performClick()
        composeTestRule.onNodeWithText(activity.getString(R.string.mindlessly_hiragana)).assertIsDisplayed()
    }

    @Test
    fun whenLearningSetsCountChanged_countStaysChanged() {
        val currentCount = 5
        val changedCount = 1
        setContent(Route.Learn(HIMIKASE.id))

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
        composeTestRule.onNodeWithContentDescription(activity.getString(R.string.navigate_back)).performClick()

        // HomeScreen()
        composeTestRule.onNodeWithText(HIMIKASE.kanaWithNakaguro).performClick()

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
        composeTestRule.onNodeWithText(activity.getString(R.string.learning_sets_n_sets, changedCount)).assertIsDisplayed()
    }

    @Test
    fun whenLearnButtonIsClicked_navigatesToQuizScreen() {
        setContent(Route.Learn(HIMIKASE.id))

        composeTestRule.onNode(isButton() and hasText(activity.getString(R.string.learn))).performClick()
        val possibleQuizHiragana =
            hasText(HI.kana) or
            hasText(MI.kana) or
            hasText(KA.kana) or
            hasText(SE.kana)
        composeTestRule.onNode(possibleQuizHiragana).assertIsDisplayed()
    }


    fun setContent(startDestination: Route = Route.Home) {
        composeTestRule.setContent {
            MindlesslyHiraganaTheme {
                DefaultNavGraph(startDestination = startDestination)
            }
        }
    }
}