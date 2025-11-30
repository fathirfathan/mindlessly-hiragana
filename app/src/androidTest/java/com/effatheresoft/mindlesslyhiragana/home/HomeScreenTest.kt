package com.effatheresoft.mindlesslyhiragana.home

import androidx.compose.material3.Surface
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.effatheresoft.mindlesslyhiragana.HiltTestActivity
import com.effatheresoft.mindlesslyhiragana.data.UserRepository
import com.effatheresoft.mindlesslyhiragana.ui.theme.MindlesslyHiraganaTheme
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import kotlinx.coroutines.test.runTest
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
    lateinit var fakeUserRepository: UserRepository

    @Before
    fun initialize() {
        hiltRule.inject()
    }

    @Test
    fun displayTopAppBar() = runTest {
        setContent()

        composeTestRule.onNodeWithText("Mindlessly Hiragana").assertIsDisplayed()
    }

    @Test
    fun displayHiraganaCategories() = runTest {
        setContent()

        val categories = listOf("ひみかせ", "ふをや", "あお", "つう・んえ", "くへ・りけ", "こに・たな", "すむ・ろる", "しいも", "とてそ", "わねれ", "のゆめぬ", "よはまほ", "さきちら", "Test All Learned")
        for (category in categories) {
            composeTestRule.onNodeWithText(category).assertIsDisplayed()
        }
    }

    @Test
    fun displayHiraganaCategoriesLockStateBasedOnProgress() = runTest{
        fakeUserRepository.updateLocalUserProgress("himikase")
        setContent()

        val unlockedCategories = listOf("ひみかせ", "Test All Learned")
        val lockedCategories = listOf("ふをや", "あお", "つう・んえ", "くへ・りけ", "こに・たな", "すむ・ろる", "しいも", "とてそ", "わねれ", "のゆめぬ", "よはまほ", "さきちら")

        for (category in unlockedCategories) {
            composeTestRule.onNodeWithContentDescription("$category category unlocked").assertIsDisplayed()
        }
        for (category in lockedCategories) {
            composeTestRule.onNodeWithContentDescription("$category category locked").assertIsDisplayed()
        }
    }

    @Test
    fun displayHiraganaCategoriesInRightOrder() = runTest {
        fakeUserRepository.updateLocalUserProgress("himikase")
        setContent()

        val categoriesOrder = listOf("ひみかせ", "Test All Learned", "ふをや", "あお", "つう・んえ", "くへ・りけ", "こに・たな", "すむ・ろる", "しいも", "とてそ", "わねれ", "のゆめぬ", "よはまほ", "さきちら")
        for (i in 0 until categoriesOrder.size - 1) {
            getTextPosition(categoriesOrder[i]).assertOnTopOf(getTextPosition(categoriesOrder[i + 1]))
        }
    }

    @Test
    fun lockedCategories_whenIsClicked_assertDoNotNavigate() = runTest {
        fakeUserRepository.updateLocalUserProgress("himikase")
        setContent()

        val lockedCategories = listOf("ふをや", "あお", "つう・んえ", "くへ・りけ", "こに・たな", "すむ・ろる", "しいも", "とてそ", "わねれ", "のゆめぬ", "よはまほ", "さきちら")
        for (category in lockedCategories) {
            composeTestRule.onNodeWithText(category).performClick()
            composeTestRule.onNodeWithText("Mindlessly Hiragana").assertIsDisplayed()
        }
    }

    fun Offset.assertOnTopOf(other: Offset) {
        assert(this.y < other.y)
    }

    fun getTextPosition(text: String): Offset {
        return composeTestRule
            .onNode(hasText(text))
            .fetchSemanticsNode()
            .positionInRoot
    }


    fun setContent() {
        composeTestRule.setContent {
            MindlesslyHiraganaTheme {
                Surface {
                    HomeScreen(
                        onNavigateToLearn = {},
                        viewModel = HomeViewModel(fakeUserRepository)
                    )
                }
            }
        }
    }
}

