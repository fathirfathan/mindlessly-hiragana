package com.effatheresoft.mindlesslyhiragana.home

import androidx.compose.material3.Surface
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
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

        composeTestRule.onNodeWithText("ひみかせ").assertIsDisplayed()
        composeTestRule.onNodeWithText("ふをや").assertIsDisplayed()
        composeTestRule.onNodeWithText("あお").assertIsDisplayed()
        composeTestRule.onNodeWithText("つう・んえ").assertIsDisplayed()
        composeTestRule.onNodeWithText("くへ・りけ").assertIsDisplayed()
        composeTestRule.onNodeWithText("こに・たな").assertIsDisplayed()
        composeTestRule.onNodeWithText("すむ・ろる").assertIsDisplayed()
        composeTestRule.onNodeWithText("しいも").assertIsDisplayed()
        composeTestRule.onNodeWithText("とてそ").assertIsDisplayed()
        composeTestRule.onNodeWithText("わねれ").assertIsDisplayed()
        composeTestRule.onNodeWithText("のゆめぬ").assertIsDisplayed()
        composeTestRule.onNodeWithText("よはまほ").assertIsDisplayed()
        composeTestRule.onNodeWithText("さきちら").assertIsDisplayed()
        composeTestRule.onNodeWithText("Test All Learned").assertIsDisplayed()
    }

    @Test
    fun displayHiraganaCategoriesLockStateBasedOnProgress() = runTest{
        fakeUserRepository.setLocalUserProgress("himikase")
        setContent()

        composeTestRule.onNodeWithContentDescription("ひみかせ category unlocked").assertIsDisplayed()
        composeTestRule.onNodeWithContentDescription("Test All Learned category unlocked").assertIsDisplayed()
        composeTestRule.onNodeWithContentDescription("ふをや category locked").assertIsDisplayed()
        composeTestRule.onNodeWithContentDescription("あお category locked").assertIsDisplayed()
        composeTestRule.onNodeWithContentDescription("つう・んえ category locked").assertIsDisplayed()
        composeTestRule.onNodeWithContentDescription("くへ・りけ category locked").assertIsDisplayed()
        composeTestRule.onNodeWithContentDescription("こに・たな category locked").assertIsDisplayed()
        composeTestRule.onNodeWithContentDescription("すむ・ろる category locked").assertIsDisplayed()
        composeTestRule.onNodeWithContentDescription("しいも category locked").assertIsDisplayed()
        composeTestRule.onNodeWithContentDescription("とてそ category locked").assertIsDisplayed()
        composeTestRule.onNodeWithContentDescription("わねれ category locked").assertIsDisplayed()
        composeTestRule.onNodeWithContentDescription("のゆめぬ category locked").assertIsDisplayed()
        composeTestRule.onNodeWithContentDescription("よはまほ category locked").assertIsDisplayed()
        composeTestRule.onNodeWithContentDescription("さきちら category locked").assertIsDisplayed()
    }

    @Test
    fun displayHiraganaCategoriesInRightOrder() = runTest {
        fakeUserRepository.setLocalUserProgress("himikase")
        setContent()

        val himikasePosition = getTextPosition("ひみかせ")
        val testAllLearnedPosition = getTextPosition("Test All Learned")
        val fuwoyaPosition = getTextPosition("ふをや")
        val aoPosition = getTextPosition("あお")
        val tsuunnePosition = getTextPosition("つう・んえ")
        val kuherikePosition = getTextPosition("くへ・りけ")
        val konitanaPosition = getTextPosition("こに・たな")
        val sumuroruPosition = getTextPosition("すむ・ろる")
        val shiimoPosition = getTextPosition("しいも")
        val totesosoPosition = getTextPosition("とてそ")
        val wanerePosition = getTextPosition("わねれ")
        val noyumenuPosition = getTextPosition("のゆめぬ")
        val yohamahoPosition = getTextPosition("よはまほ")
        val sakichiraPosition = getTextPosition("さきちら")

        himikasePosition.assertOnTopOf(testAllLearnedPosition)
        testAllLearnedPosition.assertOnTopOf(fuwoyaPosition)
        fuwoyaPosition.assertOnTopOf(aoPosition)
        aoPosition.assertOnTopOf(tsuunnePosition)
        tsuunnePosition.assertOnTopOf(kuherikePosition)
        kuherikePosition.assertOnTopOf(konitanaPosition)
        konitanaPosition.assertOnTopOf(sumuroruPosition)
        sumuroruPosition.assertOnTopOf(shiimoPosition)
        shiimoPosition.assertOnTopOf(totesosoPosition)
        totesosoPosition.assertOnTopOf(wanerePosition)
        wanerePosition.assertOnTopOf(noyumenuPosition)
        noyumenuPosition.assertOnTopOf(yohamahoPosition)
        yohamahoPosition.assertOnTopOf(sakichiraPosition)
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
                    HomeScreen(HomeViewModel(fakeUserRepository))
                }
            }
        }
    }
}

