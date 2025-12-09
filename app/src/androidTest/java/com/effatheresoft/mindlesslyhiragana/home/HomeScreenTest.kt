package com.effatheresoft.mindlesslyhiragana.home

import androidx.compose.material3.Surface
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.effatheresoft.mindlesslyhiragana.HiltTestActivity
import com.effatheresoft.mindlesslyhiragana.R
import com.effatheresoft.mindlesslyhiragana.data.HiraganaCategory
import com.effatheresoft.mindlesslyhiragana.data.HiraganaCategory.HIMIKASE
import com.effatheresoft.mindlesslyhiragana.data.HiraganaCategory.FUWOYA
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
    fun assertTopAppBarDisplayed() = runTest {
        setContent()

        composeTestRule.onNodeWithText(activity.getString(R.string.mindlessly_hiragana)).assertIsDisplayed()
    }

    @Test
    fun assertHiraganaCategoriesDisplayed() = runTest {
        setContent()

        val categories = HiraganaCategory.entries.map { it.kanaWithNakaguro } + activity.getString(R.string.test_all_learned)
        for (category in categories) {
            composeTestRule.onNodeWithText(category).assertIsDisplayed()
        }
    }

    @Test
    fun assertHiraganaCategoriesLockState_areBasedOnProgress() = runTest{
        fakeUserRepository.updateLocalUserProgress(FUWOYA.id)
        setContent()

        val unlockedCategories = listOf(HIMIKASE.kanaWithNakaguro, FUWOYA.kanaWithNakaguro, activity.getString(R.string.test_all_learned))
        val lockedCategories = HiraganaCategory.entries.drop(unlockedCategories.size - 1).map { it.kanaWithNakaguro }

        for (category in unlockedCategories) {
            composeTestRule.onNodeWithContentDescription(activity.getString(R.string.x_category_unlocked, category)).assertIsDisplayed()
        }
        for (category in lockedCategories) {
            composeTestRule.onNodeWithContentDescription(activity.getString(R.string.x_category_locked, category)).assertIsDisplayed()
        }
    }

    @Test
    fun assertHiraganaCategoriesOrderIsCorrect() = runTest {
        setContent()

        // "ひみかせ", "Test All Learned", "ふをや", ..., "よはまほ", "さきちら"
        val categoriesOrder = listOf(HIMIKASE.kanaWithNakaguro, activity.getString(R.string.test_all_learned)) + HiraganaCategory.entries.drop(1).map { it.kanaWithNakaguro }
        for (i in 0 until categoriesOrder.size - 1) {
            getTextPosition(categoriesOrder[i]).assertOnTopOf(getTextPosition(categoriesOrder[i + 1]))
        }
    }

    @Test
    fun onLockedCategories_whenIsClicked_assertDoNotNavigate() = runTest {
        setContent()

        val lockedCategories = HiraganaCategory.entries.drop(1).map { it.kanaWithNakaguro }
        for (category in lockedCategories) {
            composeTestRule.onNodeWithText(category).performClick()
            composeTestRule.onNodeWithText(activity.getString(R.string.mindlessly_hiragana)).assertIsDisplayed()
        }
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
                    HomeScreen(
                        onNavigateToLearn = {},
                        viewModel = HomeViewModel(fakeUserRepository)
                    )
                }
            }
        }
    }
}

