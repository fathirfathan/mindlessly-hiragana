package com.effatheresoft.mindlesslyhiragana.ui.home

import androidx.activity.ComponentActivity
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.junit4.AndroidComposeTestRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import com.effatheresoft.mindlesslyhiragana.data.model.HiraganaCategory
import com.effatheresoft.mindlesslyhiragana.ui.learn.isButton
import org.junit.rules.TestRule

class HomeScreenRobot <R : TestRule, A : ComponentActivity> (private val composeTestRule: AndroidComposeTestRule<R, A>) {
    val activity get() = composeTestRule.activity

    fun clickCategory(category: HiraganaCategory) {
        composeTestRule.onNodeWithText(category.kanaWithNakaguro).performClick()
    }

    fun assert_onHomeScreen() {
        composeTestRule.onNodeWithText(activity.getString(com.effatheresoft.mindlesslyhiragana.R.string.mindlessly_hiragana)).assertIsDisplayed()
    }

    fun clickTestAllLearnedButton() {
        composeTestRule.onNode(isButton() and hasText(activity.getString(com.effatheresoft.mindlesslyhiragana.R.string.test_all_learned))).performClick()
    }
}