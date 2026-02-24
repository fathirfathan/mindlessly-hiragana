package com.kaishijak.mindlesslyhiragana.test

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.kaishijak.mindlesslyhiragana.HiltTestActivity
import com.kaishijak.mindlesslyhiragana.R
import com.kaishijak.mindlesslyhiragana.data.model.HiraganaCategory
import com.kaishijak.mindlesslyhiragana.data.repository.UserRepository
import com.kaishijak.mindlesslyhiragana.navigation.DefaultNavGraph
import com.kaishijak.mindlesslyhiragana.sharedtest.util.isButton
import com.kaishijak.mindlesslyhiragana.ui.theme.MindlesslyHiraganaTheme
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
class TestScreenRoboTest {
    @get:Rule(order = 0)
    var hiltRule = HiltAndroidRule(this)

    @get:Rule(order = 1)
    val composeTestRule = createAndroidComposeRule<HiltTestActivity>()
    val activity get() = composeTestRule.activity

    @Inject lateinit var userRepository: UserRepository

    @Before
    fun init() {
        hiltRule.inject()
    }

    @Test
    fun `user click challenge learn button scenario`() = runTest {
        // given user progress is `fuwoya`
        // and learning sets count is 5
        // when user clicks `Challenge All Correct On Learn` button
        // then user navigates to learn screen
        // and user sees `ふをや`
        // and user sees `5` for learning sets
        userRepository.updateHighestCategory(HiraganaCategory.FUWOYA)
        setContentAndNavigateToTestScreen()
        composeTestRule.onNodeWithText(activity.getString(R.string.challenge_all_correct_on_learn)).performClick()
        composeTestRule.onNodeWithText("ふをや").assertIsDisplayed()
        composeTestRule.onNodeWithText(activity.getString(R.string.learning_sets_n_sets, 5)).assertIsDisplayed()
    }

    @Test
    fun `user click test all learn button scenario`() = runTest {
        // given user progress is `himikase`
        // and user isTestUnlocked is true
        // when user clicks `Test All Learned` button
        // then user navigates to test quiz screen
        // and user sees `ひ` as current question
        // and user sees `3` for remaining questions
        userRepository.updateIsTestUnlocked(true)
        setContentAndNavigateToTestScreen()
        composeTestRule.onNode(isButton() and hasText(activity.getString(R.string.test_all_learned))).performClick()
        composeTestRule.onNodeWithText("ひ").assertIsDisplayed()
        composeTestRule.onNodeWithText(activity.getString(R.string.remaining_n, 3)).assertIsDisplayed()
    }

    fun setContentAndNavigateToTestScreen() {
        composeTestRule.setContent {
            MindlesslyHiraganaTheme {
                DefaultNavGraph()
            }
        }
        composeTestRule.onNodeWithText(activity.getString(R.string.test_all_learned)).performClick()
    }
}