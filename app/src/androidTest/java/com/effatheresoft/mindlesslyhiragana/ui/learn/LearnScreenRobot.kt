package com.effatheresoft.mindlesslyhiragana.ui.learn

import androidx.activity.ComponentActivity
import androidx.compose.ui.semantics.ProgressBarRangeInfo
import androidx.compose.ui.semantics.SemanticsActions
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.hasProgressBarRangeInfo
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.junit4.AndroidComposeTestRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performSemanticsAction
import com.effatheresoft.mindlesslyhiragana.data.HiraganaCategory
import org.junit.rules.TestRule
import kotlin.properties.Delegates

class LearnScreenRobot <R : TestRule, A : ComponentActivity> (private val composeTestRule: AndroidComposeTestRule<R, A>) {
    val activity get() = composeTestRule.activity

    lateinit var category: HiraganaCategory
    var progressBarValue by Delegates.notNull<Int>()
    val progressBar get() = composeTestRule.onNode(
        matcher = hasProgressBarRangeInfo(
            rangeInfo = ProgressBarRangeInfo(
                current = progressBarValue.toFloat(),
                range = 1f..10f,
                steps = 8
            )
        )
    )
    val learnButton get() = composeTestRule.onNode(isButton() and hasText(activity.getString(com.effatheresoft.mindlesslyhiragana.R.string.learn)))

    fun assert_onLearnScreen(category: HiraganaCategory = this.category) {
        composeTestRule.onNodeWithText(activity.getString(com.effatheresoft.mindlesslyhiragana.R.string.learning_sets), substring = true).assertIsDisplayed()
        composeTestRule.onNodeWithText(category.kanaWithNakaguro).assertIsDisplayed()
    }

    fun assert_progressBar_displayed(value: Int = progressBarValue) {
        assert(progressBarValue == value)
        progressBar.assertIsDisplayed()
    }

    fun assert_progressBarLabel_displayed(value: Int) {
        composeTestRule.onNodeWithText(activity.getString(com.effatheresoft.mindlesslyhiragana.R.string.learning_sets_n_sets, value)).assertIsDisplayed()
    }

    fun click_navigateUpButton() {
        composeTestRule.onNodeWithContentDescription(activity.getString(com.effatheresoft.mindlesslyhiragana.R.string.navigate_back)).performClick()
    }

    fun click_progressBar(value: Int) {
        progressBar.performSemanticsAction(SemanticsActions.SetProgress) { it(value.toFloat()) }
        progressBarValue = value
    }

    fun click_learnButton() {
        learnButton.performClick()
    }
}