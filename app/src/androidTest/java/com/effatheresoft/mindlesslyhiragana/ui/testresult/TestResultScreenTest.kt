package com.effatheresoft.mindlesslyhiragana.ui.testresult

import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.effatheresoft.mindlesslyhiragana.HiltTestActivity
import com.effatheresoft.mindlesslyhiragana.data.model.HiraganaCategory
import com.effatheresoft.mindlesslyhiragana.data.repository.UserRepository
import com.effatheresoft.mindlesslyhiragana.navigation.NavigationViewModel
import com.effatheresoft.mindlesslyhiragana.ui.testquiz.QuestionState
import com.effatheresoft.mindlesslyhiragana.ui.theme.MindlesslyHiraganaTheme
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import jakarta.inject.Inject
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
@HiltAndroidTest
class TestResultScreenTest {
    @get:Rule
    var hiltRule = HiltAndroidRule(this)

    @get:Rule
    val composeTestRule = createAndroidComposeRule<HiltTestActivity>()

    @Inject
    lateinit var fakeUserRepository: UserRepository
    lateinit var navigationViewModel: NavigationViewModel
    private lateinit var screen: TestResultScreenRobot<ActivityScenarioRule<HiltTestActivity>, HiltTestActivity>

    @Before
    fun init() = runTest {
        hiltRule.inject()
        screen = TestResultScreenRobot(composeTestRule, fakeUserRepository)
    }

    @Test
    fun statelessUiElements_assertAreDisplayed() {
        setContent()

        screen.topAppBarTitle_assertIsDisplayed()
        screen.topAppBarNavButton_assertIsDisplayed()
        screen.tryAgainButton_assertIsDisplayed()
    }

    @Test
    fun givenCorrectCount_correctCountText_assertIsDisplayed() {
        val correctCount = 4
        setContent()

        screen.correctCountText_assertIsDisplayed(correctCount)
    }

    @Test
    fun givenIncorrectCount_correctCountText_assertIsDisplayed() {
        val incorrectCount = 0
        setContent()

        screen.incorrectCountText_assertIsDisplayed(incorrectCount)
    }

    @Test
    fun whenCanContinueLearningFalse_continueLearningButton_assertIsDisabled() = runTest {
        screen.setCanContinueLearning(false)
        setContent()

        screen.continueLearningButton_assertIsDisabled()
    }

    @Test
    fun whenCanContinueLearningTrue_continueLearningButton_assertIsEnabled() = runTest {
        screen.setCanContinueLearning(true)
        setContent()

        screen.continueLearningButton_assertIsEnabled()
    }

    fun setContent() {
        composeTestRule.setContent {
            navigationViewModel = hiltViewModel()
            navigationViewModel.setQuestionStates(
                HiraganaCategory.HIMIKASE.hiraganaList.map {
                    QuestionState(question = it, answerAttempts = listOf(it))
                }
            )
            MindlesslyHiraganaTheme {
                TestResultScreen(
                    onNavigateUp = {},
                    onTryAgain = {},
                    navigationViewModel = navigationViewModel
                )
            }
        }
    }
}