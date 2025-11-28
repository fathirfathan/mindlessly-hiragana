package com.effatheresoft.mindlesslyhiragana.navigation

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithText
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

    fun setContent() {
        composeTestRule.setContent {
            MindlesslyHiraganaTheme {
                DefaultNavGraph()
            }
        }
    }
}