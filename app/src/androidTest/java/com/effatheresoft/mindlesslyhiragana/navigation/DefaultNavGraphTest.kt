package com.effatheresoft.mindlesslyhiragana.navigation

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.effatheresoft.mindlesslyhiragana.Constants.DEFAULT_LEARNING_SETS_COUNT
import com.effatheresoft.mindlesslyhiragana.HiltTestActivity
import com.effatheresoft.mindlesslyhiragana.data.Hiragana.HI
import com.effatheresoft.mindlesslyhiragana.data.Hiragana.KA
import com.effatheresoft.mindlesslyhiragana.data.Hiragana.MI
import com.effatheresoft.mindlesslyhiragana.data.Hiragana.SE
import com.effatheresoft.mindlesslyhiragana.data.HiraganaCategory
import com.effatheresoft.mindlesslyhiragana.data.HiraganaCategory.HIMIKASE
import com.effatheresoft.mindlesslyhiragana.ui.home.HomeScreenRobot
import com.effatheresoft.mindlesslyhiragana.ui.learn.LearnScreenRobot
import com.effatheresoft.mindlesslyhiragana.ui.quiz.QuizScreenRobot
import com.effatheresoft.mindlesslyhiragana.ui.result.ResultScreenRobot
import com.effatheresoft.mindlesslyhiragana.sharedtest.util.performBackPress
import com.effatheresoft.mindlesslyhiragana.ui.theme.MindlesslyHiraganaTheme
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import junit.framework.TestCase.assertTrue
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

    private lateinit var screen: ScreenRobot<ActivityScenarioRule<HiltTestActivity>, HiltTestActivity>

    @Before
    fun init() {
        hiltRule.inject()
        val homeScreenRobot = HomeScreenRobot(composeTestRule)
        val learnScreenRobot = LearnScreenRobot(composeTestRule)
        val quizScreenRobot = QuizScreenRobot(composeTestRule)
        val resultScreenRobot = ResultScreenRobot(composeTestRule)

        learnScreenRobot.category = HIMIKASE
        learnScreenRobot.progressBarValue = DEFAULT_LEARNING_SETS_COUNT

        screen = ScreenRobot(
            home = homeScreenRobot,
            learn = learnScreenRobot,
            quiz = quizScreenRobot,
            result = resultScreenRobot
        )
    }

    @Test
    fun startDestination_isHomeScreen() {
        setContent(Route.Home)
        screen.home.assert_onHomeScreen()
    }

    @Test
    fun homeScreen_onCategoryClicked_navigatesToLearnScreen() {
        setContent(Route.Home)
        screen.navigate_homeToLearn(HIMIKASE)
    }

    @Test
    fun learnScreen_whenLearnButtonIsClicked_navigatesToQuizScreen() {
        setContent(Route.Learn(screen.learn.category.id))
        screen.navigate_learnToQuiz()
    }

    @Test
    fun quizScreen_whenAllQuizzesAreCorrectlyAnswered_navigatesToResultScreen() {
        setContent(Route.Quiz(screen.learn.category.id))
        screen.navigate_quizToResult(isAllCorrect = true)
    }

    @Test
    fun resultScreen_navigatedUpToLearnScreen_onNavigateBack_navigatesToHomeScreen() {
        setContent(Route.Home)
        screen.navigate_homeToLearn(HIMIKASE)
        screen.navigate_learnToQuiz()
        screen.navigate_quizToResult(isAllCorrect = true)
        screen.result.click_navigateUpButton()

        // don't return to quiz screen
        screen.learn.assert_onLearnScreen()
        composeTestRule.performBackPress()
        screen.home.assert_onHomeScreen()
    }

    @Test
    fun learnScreen_onNavigateUpButtonClicked_navigatesToHomeScreen() {
        setContent(Route.Home)
        screen.navigate_homeToLearn(HIMIKASE)
        screen.navigateBack_learnToHome()
    }

    @Test
    fun learnScreen_navigatedUpToHome_onNavigateBack_exitApp() {
        setContent(Route.Home)
        screen.navigate_homeToLearn(HIMIKASE)
        screen.navigateBack_learnToHome()

        composeTestRule.performBackPress()
        assertTrue(activity.isFinishing || activity.isDestroyed)
    }

    @Test
    fun quizScreen_onNavigateUpButtonClicked_navigatesToLearnScreen() {
        setContent(Route.Learn(screen.learn.category.id))
        screen.navigate_learnToQuiz()
        screen.navigateBack_quizToLearn()
    }

    @Test
    fun quizScreen_navigatedUpToLearnScreen_onNavigateBack_navigatesToHomeScreen() {
        setContent(Route.Home)
        screen.navigate_homeToLearn(HIMIKASE)
        screen.navigate_learnToQuiz()
        screen.navigateBack_quizToLearn()

        composeTestRule.performBackPress()
        screen.home.assert_onHomeScreen()
    }

    @Test
    fun learnScreen_whenLearningSetsCountChanged_countStaysChanged() {
        val currentCount = 5
        val changedCount = 1

        setContent(Route.Home)
        screen.navigate_homeToLearn(HIMIKASE)

        screen.learn.progressBarValue = currentCount
        screen.learn.click_progressBar(changedCount)

        screen.navigateBack_learnToHome()
        screen.navigate_homeToLearn(HIMIKASE)

        screen.learn.assert_progressBar_displayed(changedCount)
        screen.learn.assert_progressBarLabel_displayed(changedCount)
    }

    @Test
    fun quizScreen_whenAllQuizzesAreCorrectlyAnswered_navigatesToResultScreen_assertResultCountsAreCorrect() {
        setContent(Route.Quiz(screen.learn.category.id))
        screen.navigate_quizToResult(isAllCorrect = true)

        screen.result.assert_correctCount_displayed(20)
        screen.result.assert_incorrectCount_displayed(0)

        composeTestRule.onNodeWithText("${HI.kana}: 0").assertIsDisplayed()
        composeTestRule.onNodeWithText("${MI.kana}: 0").assertIsDisplayed()
        composeTestRule.onNodeWithText("${KA.kana}: 0").assertIsDisplayed()
        composeTestRule.onNodeWithText("${SE.kana}: 0").assertIsDisplayed()
    }

    fun setContent(startDestination: Route) {
        when (startDestination) {
            is Route.Learn -> screen.learn.category = HiraganaCategory.entries.first { it.id == startDestination.categoryId }
            is Route.Quiz -> screen.learn.category = HiraganaCategory.entries.first { it.id == startDestination.categoryId }
            is Route.Result -> screen.learn.category = HiraganaCategory.entries.first { it.id == startDestination.categoryId }
            else -> {}
        }
        composeTestRule.setContent {
            MindlesslyHiraganaTheme {
                DefaultNavGraph(startDestination = startDestination)
            }
        }
    }
}