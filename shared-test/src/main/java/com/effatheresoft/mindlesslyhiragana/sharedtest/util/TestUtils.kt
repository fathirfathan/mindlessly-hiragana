package com.effatheresoft.mindlesslyhiragana.sharedtest.util

import androidx.compose.ui.test.junit4.AndroidComposeTestRule
import androidx.test.ext.junit.rules.ActivityScenarioRule
import com.effatheresoft.mindlesslyhiragana.HiltTestActivity

fun AndroidComposeTestRule<ActivityScenarioRule<HiltTestActivity>, HiltTestActivity>.performBackPress() {
    runOnUiThread {
        activity.onBackPressedDispatcher.onBackPressed()
    }
}