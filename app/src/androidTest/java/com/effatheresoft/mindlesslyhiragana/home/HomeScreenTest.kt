package com.effatheresoft.mindlesslyhiragana.home

import androidx.compose.material3.Surface
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.effatheresoft.mindlesslyhiragana.HiltTestActivity
import com.effatheresoft.mindlesslyhiragana.data.UserRepository
import com.effatheresoft.mindlesslyhiragana.ui.theme.MindlesslyHiraganaTheme
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import javax.inject.Inject

@RunWith(AndroidJUnit4::class)
@HiltAndroidTest
class HomeScreenTest {
    @get:Rule(order = 0)
    var hiltRule = HiltAndroidRule(this)

    @get:Rule(order = 1)
    val composeTestRule = createAndroidComposeRule<HiltTestActivity>()
    private val activity get() = composeTestRule.activity

    @Inject
    lateinit var userRepository: UserRepository

    @Before
    fun initialize() {
        hiltRule.inject()
    }

    @Test
    fun displayTopAppBar() {
        setContent()

        composeTestRule.onNodeWithText("Mindlessly Hiragana").assertIsDisplayed()
    }

    @Test
    fun displayHiraganaCategories() {
        setContent()

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

    @Test
    fun displayHiraganaCategoriesLockState_withLocalUserProgress() {
        setContent()

        composeTestRule.onNodeWithContentDescription("ひみかせ category unlocked").assertIsDisplayed()
        composeTestRule.onNodeWithContentDescription("Test All Learned category unlocked").assertIsDisplayed()
        composeTestRule.onNodeWithContentDescription("ふをや category locked").assertIsDisplayed()
        composeTestRule.onNodeWithContentDescription("あお category locked").assertIsDisplayed()
        composeTestRule.onNodeWithContentDescription("つう・んえ category locked").assertIsDisplayed()
        composeTestRule.onNodeWithContentDescription("くへ・りけ category locked").assertIsDisplayed()
        composeTestRule.onNodeWithContentDescription("こに・たな category locked").assertIsDisplayed()
        composeTestRule.onNodeWithContentDescription("すむ・ろる category locked").assertIsDisplayed()
        composeTestRule.onNodeWithContentDescription("しいも category locked").assertIsDisplayed()
        composeTestRule.onNodeWithContentDescription("とてそ category locked").assertIsDisplayed()
        composeTestRule.onNodeWithContentDescription("わねれ category locked").assertIsDisplayed()
        composeTestRule.onNodeWithContentDescription("のゆめぬ category locked").assertIsDisplayed()
        composeTestRule.onNodeWithContentDescription("よはまほ category locked").assertIsDisplayed()
        composeTestRule.onNodeWithContentDescription("さきちら category locked").assertIsDisplayed()

        val himikasePosition = composeTestRule
            .onNode(hasText("ひみかせ"))
            .fetchSemanticsNode()
            .positionInRoot
        val testAllLearnedPosition = composeTestRule
            .onNode(hasText("Test All Learned"))
            .fetchSemanticsNode()
            .positionInRoot
        val fuwoyaPosition = composeTestRule
            .onNode(hasText("ふをや"))
            .fetchSemanticsNode()
            .positionInRoot

        // `Test All Learned` is between `ひみかせ` and `ふをや`
        assert(himikasePosition.y < testAllLearnedPosition.y)
        assert(testAllLearnedPosition.y < fuwoyaPosition.y)
    }

    fun setContent() {
        composeTestRule.setContent {
            MindlesslyHiraganaTheme {
                Surface {
                    HomeScreen(HomeViewModel(userRepository))
                }
            }
        }
    }
}

