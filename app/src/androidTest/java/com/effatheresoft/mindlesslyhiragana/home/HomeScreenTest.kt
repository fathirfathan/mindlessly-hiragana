package com.effatheresoft.mindlesslyhiragana.home

import androidx.compose.material3.Surface
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
class HomeScreenTest {
    @get:Rule(order = 0)
    var hiltRule = HiltAndroidRule(this)

    @get:Rule(order = 1)
    val composeTestRule = createAndroidComposeRule<HiltTestActivity>()
    private val activity get() = composeTestRule.activity

    @Before
    fun initialize() {
        hiltRule.inject()
    }

    @Test
    fun displayTopAppBar() {
        composeTestRule.setContent {
            MindlesslyHiraganaTheme {
                Surface {
                    HomeScreen()
                }
            }
        }
        composeTestRule.onNodeWithText("Mindlessly Hiragana").assertIsDisplayed()
    }

    @Test
    fun displayHiraganaCategories() {
        composeTestRule.setContent {
            MindlesslyHiraganaTheme {
                Surface {
                    HomeScreen()
                }
            }
        }

        composeTestRule.onNodeWithText("ひみかせ").assertIsDisplayed()
        composeTestRule.onNodeWithText("ふをや").assertIsDisplayed()
        composeTestRule.onNodeWithText("あお").assertIsDisplayed()
        composeTestRule.onNodeWithText("つう・んえ").assertIsDisplayed()
        composeTestRule.onNodeWithText("くへ・りけ").assertIsDisplayed()
        composeTestRule.onNodeWithText("こに・たな").assertIsDisplayed()
        composeTestRule.onNodeWithText("すむ・ろる").assertIsDisplayed()
        composeTestRule.onNodeWithText("しいも").assertIsDisplayed()
        composeTestRule.onNodeWithText("とてそ").assertIsDisplayed()
        composeTestRule.onNodeWithText("わねれ").assertIsDisplayed()
        composeTestRule.onNodeWithText("のゆめぬ").assertIsDisplayed()
        composeTestRule.onNodeWithText("よはまほ").assertIsDisplayed()
        composeTestRule.onNodeWithText("さきちら").assertIsDisplayed()
        composeTestRule.onNodeWithText("Test All Learned").assertIsDisplayed()
    }
}

