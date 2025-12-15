package com.effatheresoft.mindlesslyhiragana.ui.test

import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.effatheresoft.mindlesslyhiragana.HiltTestActivity
import com.effatheresoft.mindlesslyhiragana.data.model.HiraganaCategory.FUWOYA
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
class TestScreenTest {

    @get:Rule(order = 0)
    var hiltRule = HiltAndroidRule(this)

    @get:Rule(order = 1)
    val composeTestRule = createAndroidComposeRule<HiltTestActivity>()

    @Inject
    lateinit var fakeUserRepository: UserRepository

    private lateinit var screen: TestScreenRobot<ActivityScenarioRule<HiltTestActivity>, HiltTestActivity>

    @Before
    fun init() = runTest {
        hiltRule.inject()
        screen = TestScreenRobot(composeTestRule, fakeUserRepository)
        screen.setProgress(FUWOYA.id)
        screen.setIsTestUnlocked(false)
    }

    @Test
    fun statelessUiElements_assertAreDisplayed() {
        setContent()

        screen.topAppBarTitle_assertIsDisplayed()
        screen.topAppBarNavButton_assertIsDisplayed()

        screen.questionsText_assertIsDisplayed()
        screen.includingText_assertIsDisplayed()

        screen.challengeButton_assertIsDisplayed()
        screen.testButton_assertIsDisplayed()
    }

    @Test
    fun givenCategoryList_categoryList_QuestionsCount_assertAreDisplayed() = runTest {
        setContent()
        screen.questionsText_assertIsDisplayed(2)
        screen.categoryText_assertIsDisplayed()
    }

    @Test
    fun whenTestIsUnlocked_testButton_assertIsEnabled() = runTest {
        screen.setIsTestUnlocked(true)
        setContent()
        screen.testButton_assertIsEnabled()
    }

    @Test
    fun whenTestIsLocked_testButton_assertIsDisabled() = runTest {
        screen.setIsTestUnlocked(false)
        setContent()
        screen.testButton_assertIsDisabled()
    }

    fun setContent() {
        composeTestRule.setContent {
            MindlesslyHiraganaTheme {
                TestScreen(
                    onNavigationIconClick = {},
                    onChallengeLearn = {}
                )
            }
        }
    }
}