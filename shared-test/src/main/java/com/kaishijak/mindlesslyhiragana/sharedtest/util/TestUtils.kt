package com.kaishijak.mindlesslyhiragana.sharedtest.util

import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.SemanticsProperties
import androidx.compose.ui.test.SemanticsMatcher
import androidx.compose.ui.test.junit4.AndroidComposeTestRule
import androidx.test.ext.junit.rules.ActivityScenarioRule
import com.kaishijak.mindlesslyhiragana.HiltTestActivity

fun AndroidComposeTestRule<ActivityScenarioRule<HiltTestActivity>, HiltTestActivity>.performBackPress() {
    runOnUiThread {
        activity.onBackPressedDispatcher.onBackPressed()
    }
}

fun isButton() = SemanticsMatcher.expectValue(SemanticsProperties.Role, Role.Button)