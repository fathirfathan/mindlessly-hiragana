package com.effatheresoft.mindlesslyhiragana.home

import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onAllNodesWithText
import androidx.compose.ui.test.onNodeWithText
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.effatheresoft.mindlesslyhiragana.HiltTestActivity
import com.effatheresoft.mindlesslyhiragana.MainActivity
import com.effatheresoft.mindlesslyhiragana.R
import com.effatheresoft.mindlesslyhiragana.data.model.HiraganaCategory.HIMIKASE
import com.effatheresoft.mindlesslyhiragana.data.repository.UserRepository
import com.effatheresoft.mindlesslyhiragana.ui.home.HomeScreen
import com.effatheresoft.mindlesslyhiragana.ui.home.HomeViewModel
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
class HomeScreenRoboTest {
    @get:Rule(order = 0)
    var hiltRule = HiltAndroidRule(this)

    @get:Rule(order = 1)
//    val composeTestRule = createComposeRule()
    val composeTestRule = createAndroidComposeRule<MainActivity>()
//    private val activity get() = composeTestRule.activity

//    @Inject
//    lateinit var fakeUserRepository: UserRepository

//    @Before
//    fun initialize() = runTest {
//        hiltRule.inject()
//    }

    @Test
    fun testAssertFailed() {
//        setContent()
        composeTestRule.onNodeWithText("Hello World!").assertIsDisplayed()
    }

    @Test
    fun testAssertFailed2() {
        composeTestRule.onNodeWithText("Hello World!").assertIsDisplayed()
    }

    @Test
    fun testAssertSuccess() {
        composeTestRule.onNodeWithText("Hello World").assertIsDisplayed()
    }

//    @Test
//    fun assertTopAppBarDisplayed() = runTest {
//        setContent()
//
//        composeTestRule.onAllNodesWithText(activity.getString(R.string.mindlessly_hiragana))[0].assertIsDisplayed()
//    }

    fun setContent() {
        composeTestRule.setContent {
            MindlesslyHiraganaTheme {
                Surface {
                    TestComposable()
//                    HomeScreen(
//                        onNavigateToLearn = {},
//                        onNavigateToTest = {},
//                        viewModel = HomeViewModel(fakeUserRepository)
//                    )
                }
            }
        }
    }
}

@Composable
fun TestComposable() {
//    Text("Hello World")
}