package com.effatheresoft.mindlesslyhiragana.ui.result

import androidx.compose.ui.test.assertCountEquals
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onAllNodesWithText
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.effatheresoft.mindlesslyhiragana.HiltTestActivity
import com.effatheresoft.mindlesslyhiragana.R
import com.effatheresoft.mindlesslyhiragana.data.model.Hiragana.HI
import com.effatheresoft.mindlesslyhiragana.data.model.Hiragana.MI
import com.effatheresoft.mindlesslyhiragana.data.model.Hiragana.KA
import com.effatheresoft.mindlesslyhiragana.data.model.Hiragana.SE
import com.effatheresoft.mindlesslyhiragana.data.repository.QuizRepository
import com.effatheresoft.mindlesslyhiragana.data.repository.UserRepository
import com.effatheresoft.mindlesslyhiragana.ui.learn.isButton
import com.effatheresoft.mindlesslyhiragana.sharedtest.data.FakeQuizRepository
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
class ResultScreenTest {

    @get:Rule(order = 0)
    var hiltRule = HiltAndroidRule(this)

    @get:Rule(order = 1)
    val composeTestRule = createAndroidComposeRule<HiltTestActivity>()
    private val activity get() = composeTestRule.activity

    lateinit var fakeQuizRepository: QuizRepository

    @Inject
    lateinit var fakeUserRepository: UserRepository
    lateinit var screen: ResultScreenRobot<ActivityScenarioRule<HiltTestActivity>, HiltTestActivity>

    @Before
    fun init() {
        hiltRule.inject()
        fakeQuizRepository = FakeQuizRepository(isPrepopulated = true)
        screen = ResultScreenRobot(composeTestRule, fakeUserRepository)
    }

    @Test
    fun topAppBar_assertIsDisplayed() {
        setContent()

        composeTestRule.onNodeWithText(activity.getString(R.string.result)).assertIsDisplayed()
        composeTestRule.onNodeWithContentDescription(activity.getString(R.string.navigate_back)).assertIsDisplayed()
    }

    @Test
    fun basicUiElements_assertIsDisplayed() {
        setContent(fakeQuizRepository)

        composeTestRule.onNodeWithText("Correct", substring = true).assertIsDisplayed()
        composeTestRule.onAllNodesWithText("Incorrect", substring = true).assertCountEquals(2)
        composeTestRule.onNodeWithText(HI.kana, substring = true).assertIsDisplayed()
        composeTestRule.onNodeWithText(MI.kana, substring = true).assertIsDisplayed()
        composeTestRule.onNodeWithText(KA.kana, substring = true).assertIsDisplayed()
        composeTestRule.onNodeWithText(SE.kana, substring = true).assertIsDisplayed()
        composeTestRule.onNode(isButton() and hasText("Try Again")).assertIsDisplayed()
        composeTestRule.onNode(isButton() and hasText("Test All Learned")).assertIsDisplayed()
    }

    @Test
    fun withQuizzes_counts_assertAreDisplayed() {
        setContent(fakeQuizRepository)

        composeTestRule.onNodeWithText("Correct: 4").assertIsDisplayed()
        composeTestRule.onNodeWithText("Incorrect: 4").assertIsDisplayed()
        composeTestRule.onNodeWithText("Incorrect Counts").assertIsDisplayed()
        composeTestRule.onNodeWithText("${HI.kana}: 0").assertIsDisplayed()
        composeTestRule.onNodeWithText("${MI.kana}: 2").assertIsDisplayed()
        composeTestRule.onNodeWithText("${KA.kana}: 0").assertIsDisplayed()
        composeTestRule.onNodeWithText("${SE.kana}: 2").assertIsDisplayed()
    }

    @Test
    fun whenLocalUserIsTestUnlockedTrue_testAllLearnedButton_assertIsEnabled() = runTest {
        screen.setLocalUserIsTestUnlocked(true)
        setContent(fakeQuizRepository)
        screen.assert_testAllLearnedButton_enabled()
    }

    fun setContent() {
        composeTestRule.setContent {
            MindlesslyHiraganaTheme {
                ResultScreen(onNavigateUp = {}, onTryAgain = {}, onTestAllLearned = {})
            }
        }
    }

    fun setContent(fakeQuizRepository: QuizRepository) {
        composeTestRule.setContent {
            MindlesslyHiraganaTheme {
                ResultScreen(
                    onNavigateUp = {},
                    onTryAgain = {},
                    onTestAllLearned = {},
                    viewModel = ResultViewModel(
                        quizRepository = fakeQuizRepository,
                        userRepository = fakeUserRepository
                    )
                )
            }
        }
    }
}