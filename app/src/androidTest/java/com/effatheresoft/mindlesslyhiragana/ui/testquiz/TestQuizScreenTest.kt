package com.effatheresoft.mindlesslyhiragana.ui.testquiz

import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.effatheresoft.mindlesslyhiragana.HiltTestActivity
import com.effatheresoft.mindlesslyhiragana.data.repository.UserRepository
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
class TestQuizScreenTest {
    @get:Rule(order = 0)
    var hiltRule = HiltAndroidRule(this)

    @get:Rule(order = 1)
    val composeTestRule = createAndroidComposeRule<HiltTestActivity>()

    @Inject
    lateinit var fakeUserRepository: UserRepository
    private lateinit var screen: TestQuizScreenRobot<ActivityScenarioRule<HiltTestActivity>, HiltTestActivity>

    @Before
    fun init() = runTest {
        hiltRule.inject()
        screen = TestQuizScreenRobot(composeTestRule, fakeUserRepository)
    }

    @Test
    fun statelessUiElements_assertAreDisplayed() {
        setContent()

        screen.topAppBarTitle_assertIsDisplayed()
        screen.topAppBarNavButton_assertIsDisplayed()
        screen.answerButtons_assertAreDisplayed()
    }

    @Test
    fun givenQuestion_questionText_assertIsDisplayed() {
        val question = "ひ"
        setContent()
        screen.questionText_assertIsDisplayed(question)
    }

    @Test
    fun givenRemainingQuestionsCount_remainingText_assertIsDisplayed() {
        val remainingQuestionsCount = 3
        setContent()
        screen.remainingText_assertIsDisplayed(remainingQuestionsCount)
    }

    fun setContent() {
        composeTestRule.setContent {
            MindlesslyHiraganaTheme {
                TestQuizScreen(onNavigateUp = {})
            }
        }
    }
}