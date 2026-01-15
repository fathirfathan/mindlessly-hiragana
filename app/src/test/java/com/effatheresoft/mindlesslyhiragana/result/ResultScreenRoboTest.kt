package com.effatheresoft.mindlesslyhiragana.result

import androidx.compose.ui.semantics.ProgressBarRangeInfo
import androidx.compose.ui.semantics.SemanticsActions
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertIsNotDisplayed
import androidx.compose.ui.test.hasProgressBarRangeInfo
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performSemanticsAction
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.effatheresoft.mindlesslyhiragana.HiltTestActivity
import com.effatheresoft.mindlesslyhiragana.R
import com.effatheresoft.mindlesslyhiragana.data.repository.RefactoredQuizRepository
import com.effatheresoft.mindlesslyhiragana.data.repository.RefactoredUserRepository
import com.effatheresoft.mindlesslyhiragana.navigation.DefaultNavGraph
import com.effatheresoft.mindlesslyhiragana.sharedtest.util.isButton
import com.effatheresoft.mindlesslyhiragana.ui.theme.MindlesslyHiraganaTheme
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import dagger.hilt.android.testing.HiltTestApplication
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.annotation.Config
import javax.inject.Inject

@RunWith(AndroidJUnit4::class)
@HiltAndroidTest
@Config(application = HiltTestApplication::class)
class ResultScreenRoboTest {
    @get:Rule(order = 0)
    var hiltRule = HiltAndroidRule(this)

    @get:Rule(order = 1)
    val composeTestRule = createAndroidComposeRule<HiltTestActivity>()
    val activity get() = composeTestRule.activity

    @Inject lateinit var userRepository: RefactoredUserRepository
    @Inject lateinit var quizRepository: RefactoredQuizRepository

    @Before
    fun init() {
        hiltRule.inject()
    }

    @Test
    fun `user click try again button scenario`() = runTest {
        // given user progress is `himikase`
        // and selected category is `ひみかせ`
        // and learning sets count is 1
        // when user clicks `Try Again` button
        // then user navigates to quiz screen
        // and user sees `HI` as possible answer
        // and user have three remaining questions count
        setContentAndNavigateToResultScreen("ひみかせ", 1)
        composeTestRule.onNodeWithText(activity.getString(R.string.try_again)).performClick()

        composeTestRule.onNodeWithText("HI").assertIsDisplayed()
        composeTestRule.onNodeWithText(activity.getString(R.string.remaining_n, 3)).assertIsDisplayed()
    }

    @Test
    fun `user click test all learned button scenario`() = runTest {
        // given user progress is `himikase`
        // and selected category is `ひみかせ`
        // and learning sets count is 1
        // when user clicks `Test All Learned` button
        // then user navigates to test screen
        setContentAndNavigateToResultScreen("ひみかせ", 1)
        composeTestRule.onNodeWithText(activity.getString(R.string.test_all_learned)).performClick()

        composeTestRule.onNodeWithText(activity.getString(R.string.test_categories_n, 1)).assertIsDisplayed()
        composeTestRule.onNodeWithText("ひみかせ").assertIsDisplayed()
        composeTestRule.onNodeWithText("ふをや").assertIsNotDisplayed()
    }

    fun setContentAndNavigateToResultScreen(category: String, learningSetsCount: Int) {
        composeTestRule.setContent {
            MindlesslyHiraganaTheme {
                DefaultNavGraph()
            }
        }
        composeTestRule.onNodeWithText(category).performClick()
        composeTestRule.onNode(
            hasProgressBarRangeInfo(
                ProgressBarRangeInfo(
                    current = 5f,
                    range = 1f..10f,
                    steps = 8
                )
            )
        ).performSemanticsAction(SemanticsActions.SetProgress) { it(learningSetsCount.toFloat()) }
        composeTestRule.onNode(isButton() and hasText(activity.getString(R.string.learn))).performClick()
        when(category) {
            "ひみかせ" -> listOf("HI", "MI", "KA", "SE")
            "ふをや" -> listOf("FU", "WO", "YA")
            "あお" -> listOf("A", "O")
            "つう・んえ" -> listOf("TSU", "U", "N", "E")
            "くへ・りけ" -> listOf("KU", "HE", "RI", "KE")
            "こに・たな" -> listOf("KO", "NI", "TA", "NA")
            "すむ・ろる" -> listOf("SU", "MU", "RO", "RU")
            "しいも" -> listOf("SHI", "I", "MO")
            "とてそ" -> listOf("TO", "TE", "SO")
            "わねれ" -> listOf("WA", "NE", "RE")
            "のゆめぬ" -> listOf("NO", "YU", "ME", "NU")
            "よはまほ" -> listOf("YO", "HA", "MA", "HO")
            "さきちら" -> listOf("SA", "KI", "CHI", "RA")
            else -> listOf()
        }.forEach {
            composeTestRule.onNodeWithText(it).performClick()
        }
    }
}