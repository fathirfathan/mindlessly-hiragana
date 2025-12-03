package com.effatheresoft.mindlesslyhiragana.quiz

import androidx.compose.material3.Surface
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.effatheresoft.mindlesslyhiragana.HiltTestActivity
import com.effatheresoft.mindlesslyhiragana.R
import com.effatheresoft.mindlesslyhiragana.data.Hiragana.HI
import com.effatheresoft.mindlesslyhiragana.data.Hiragana.KA
import com.effatheresoft.mindlesslyhiragana.data.Hiragana.MI
import com.effatheresoft.mindlesslyhiragana.data.Hiragana.SE
import com.effatheresoft.mindlesslyhiragana.data.HiraganaCategory.HIMIKASE
import com.effatheresoft.mindlesslyhiragana.data.UserRepository
import com.effatheresoft.mindlesslyhiragana.ui.theme.MindlesslyHiraganaTheme
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import javax.inject.Inject

@RunWith(AndroidJUnit4::class)
@HiltAndroidTest
class QuizScreenTest {

    @get:Rule(order = 0)
    var hiltRule = HiltAndroidRule(this)

    @get:Rule(order = 1)
    val composeTestRule = createAndroidComposeRule<HiltTestActivity>()
    private val activity get() = composeTestRule.activity

    @Inject
    lateinit var fakeUserRepository: UserRepository

    @Before
    fun init() {
        hiltRule.inject()
    }

    @Test
    fun topAppBar_assertIsDisplayed() {
        setContent()

        composeTestRule.onNodeWithText(activity.getString(R.string.learn)).assertIsDisplayed()
        composeTestRule.onNodeWithContentDescription(activity.getString(R.string.navigate_back)).assertIsDisplayed()
    }

    @Test
    fun quizHiragana_assertIsDisplayed() {
        setContent()
        val possibleQuizHiragana =
            hasText(HI.kana) or
            hasText(MI.kana) or
            hasText(KA.kana) or
            hasText(SE.kana)
        composeTestRule.onNode(possibleQuizHiragana).assertIsDisplayed()
    }

    @Test
    fun remainingQuestionsCount_assertIsDisplayed() = runTest {
        val viewModel = QuizViewModel(fakeUserRepository)
        setContent(viewModel)

        val remainingQuestionsCount = viewModel.uiState.first().remainingQuestionsCount
        composeTestRule.onNodeWithText("Remaining: $remainingQuestionsCount").assertIsDisplayed()
    }
    
    @Test
    fun answerButtons_assertIsDisplayed() = runTest {
        setContent()

        val possibleAnswers = listOf(HI.name, MI.name, KA.name, SE.name)
        for (answer in possibleAnswers) {
            composeTestRule.onNodeWithText(answer).assertIsDisplayed()
        }
    }

    fun setContent() {
        composeTestRule.setContent {
            MindlesslyHiraganaTheme {
                Surface {
                    QuizScreen(categoryId = HIMIKASE.id)
                }
            }
        }
    }

    fun setContent(viewModel: QuizViewModel) {
        composeTestRule.setContent {
            MindlesslyHiraganaTheme {
                Surface {
                    QuizScreen(categoryId = HIMIKASE.id, viewModel = viewModel)
                }
            }
        }
    }
}