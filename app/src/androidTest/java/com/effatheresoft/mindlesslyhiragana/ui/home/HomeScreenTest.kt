package com.effatheresoft.mindlesslyhiragana.ui.home
//
//import androidx.compose.material3.Surface
//import androidx.compose.ui.geometry.Offset
//import androidx.compose.ui.test.assertIsDisplayed
//import androidx.compose.ui.test.hasText
//import androidx.compose.ui.test.junit4.createAndroidComposeRule
//import androidx.compose.ui.test.onAllNodesWithText
//import androidx.compose.ui.test.onNodeWithContentDescription
//import androidx.compose.ui.test.onNodeWithText
//import androidx.compose.ui.test.performClick
//import androidx.test.ext.junit.rules.ActivityScenarioRule
//import androidx.test.ext.junit.runners.AndroidJUnit4
//import com.effatheresoft.mindlesslyhiragana.HiltTestActivity
//import com.effatheresoft.mindlesslyhiragana.R
//import com.effatheresoft.mindlesslyhiragana.data.model.HiraganaCategory
//import com.effatheresoft.mindlesslyhiragana.data.model.HiraganaCategory.HIMIKASE
//import com.effatheresoft.mindlesslyhiragana.data.model.HiraganaCategory.FUWOYA
//import com.effatheresoft.mindlesslyhiragana.data.repository.UserRepository
//import com.effatheresoft.mindlesslyhiragana.ui.theme.MindlesslyHiraganaTheme
//import dagger.hilt.android.testing.HiltAndroidRule
//import dagger.hilt.android.testing.HiltAndroidTest
//import junit.framework.TestCase.assertEquals
//import kotlinx.coroutines.flow.first
//import kotlinx.coroutines.test.runTest
//import org.junit.Before
//import org.junit.Rule
//import org.junit.Test
//import org.junit.runner.RunWith
//import javax.inject.Inject
//
//@RunWith(AndroidJUnit4::class)
//@HiltAndroidTest
//class HomeScreenTest {
//    @get:Rule(order = 0)
//    var hiltRule = HiltAndroidRule(this)
//
//    @get:Rule(order = 1)
//    val composeTestRule = createAndroidComposeRule<HiltTestActivity>()
//    private val activity get() = composeTestRule.activity
//
//    @Inject
//    lateinit var fakeUserRepository: UserRepository
//    private lateinit var screen: HomeScreenRobot<ActivityScenarioRule<HiltTestActivity>, HiltTestActivity>
//
//    @Before
//    fun initialize() = runTest {
//        hiltRule.inject()
//        screen = HomeScreenRobot(composeTestRule, fakeUserRepository)
//        screen.setLocalUserProgress(HIMIKASE.id)
//    }
//
//    @Test
//    fun assertTopAppBarDisplayed() = runTest {
//        setContent()
//
//        composeTestRule.onAllNodesWithText(activity.getString(R.string.mindlessly_hiragana))[0].assertIsDisplayed()
//    }
//
//    @Test
//    fun assertHiraganaCategoriesDisplayed() = runTest {
//        setContent()
//
//        val categories = HiraganaCategory.entries.map { it.kanaWithNakaguro } + activity.getString(R.string.test_all_learned)
//        for (category in categories) {
//            composeTestRule.onNodeWithText(category).assertIsDisplayed()
//        }
//    }
//
//    @Test
//    fun assertHiraganaCategoriesLockState_areBasedOnProgress() = runTest{
//        fakeUserRepository.updateLocalUserProgress(FUWOYA.id)
//        setContent()
//
//        val unlockedCategories = listOf(HIMIKASE.kanaWithNakaguro, FUWOYA.kanaWithNakaguro, activity.getString(R.string.test_all_learned))
//        val lockedCategories = HiraganaCategory.entries.drop(unlockedCategories.size - 1).map { it.kanaWithNakaguro }
//
//        for (category in unlockedCategories) {
//            composeTestRule.onNodeWithContentDescription(activity.getString(R.string.x_category_unlocked, category)).assertIsDisplayed()
//        }
//        for (category in lockedCategories) {
//            composeTestRule.onNodeWithContentDescription(activity.getString(R.string.x_category_locked, category)).assertIsDisplayed()
//        }
//    }
//
//    @Test
//    fun assertHiraganaCategoriesOrderIsCorrect() = runTest {
//        setContent()
//
//        // "ひみかせ", "Test All Learned", "ふをや", ..., "よはまほ", "さきちら"
//        val categoriesOrder = listOf(HIMIKASE.kanaWithNakaguro, activity.getString(R.string.test_all_learned)) + HiraganaCategory.entries.drop(1).map { it.kanaWithNakaguro }
//        for (i in 0 until categoriesOrder.size - 1) {
//            getTextPosition(categoriesOrder[i]).assertOnTopOf(getTextPosition(categoriesOrder[i + 1]))
//        }
//    }
//
//    @Test
//    fun onLockedCategories_whenIsClicked_assertDoNotNavigate() = runTest {
//        setContent()
//
//        val lockedCategories = HiraganaCategory.entries.drop(1).map { it.kanaWithNakaguro }
//        for (category in lockedCategories) {
//            composeTestRule.onNodeWithText(category).performClick()
//            composeTestRule.onAllNodesWithText(activity.getString(R.string.mindlessly_hiragana))[0].assertIsDisplayed()
//        }
//    }
//
//    fun Offset.assertOnTopOf(other: Offset) {
//        assert(this.y < other.y)
//    }
//
//    fun getTextPosition(text: String): Offset {
//        return composeTestRule
//            .onNode(hasText(text))
//            .fetchSemanticsNode()
//            .positionInRoot
//    }
//
//    @Test
//    fun topAppBarMenu_assertIsDisplayed() {
//        setContent()
//        screen.topAppBarMenu_assertIsDisplayed()
//    }
//
//    @Test
//    fun topAppBarMenu_whenIsClicked_drawer_assertIsDisplayed() {
//        setContent()
//        screen.topAppBarMenu_click()
//        screen.drawer_assertIsDisplayed()
//    }
//
//    @Test
//    fun drawerStatelessUiElements_assertIsDisplayed() {
//        setContent()
//        screen.topAppBarMenu_click()
//
//        screen.drawerTitleText_assertIsDisplayed()
//        screen.drawerResetProgressButton_assertIsDisplayed()
//    }
//
//    @Test
//    fun drawerResetProgressButton_whenIsClicked_resetDialog_assertIsDisplayed() {
//        setContent()
//        screen.topAppBarMenu_click()
//
//        screen.drawerResetProgressButton_click()
//        screen.resetDialog_assertIsDisplayed()
//    }
//
//    @Test
//    fun resetDialogStatelessUiElements_assertIsDisplayed() {
//        setContent()
//        screen.topAppBarMenu_click()
//        screen.drawerResetProgressButton_click()
//
//        screen.resetDialogTitleText_assertIsDisplayed()
//        screen.resetDialogText_assertIsDisplayed()
//        screen.resetDialogConfirmButton_assertIsDisplayed()
//        screen.resetDialogCancelButton_assertIsDisplayed()
//    }
//
//    @Test
//    fun resetDialog_whenCancelButtonIsClicked_dialog_assertIsDismissed() {
//        setContent()
//        screen.topAppBarMenu_click()
//        screen.drawerResetProgressButton_click()
//
//        screen.resetDialogCancelButton_click()
//        screen.resetDialog_assertIsNotDisplayed()
//    }
//
//    @Test
//    fun resetDialog_whenConfirmButtonIsClicked_dialog_assertIsDismissed() {
//        setContent()
//        screen.topAppBarMenu_click()
//        screen.drawerResetProgressButton_click()
//
//        screen.resetDialogConfirmButton_click()
//        screen.resetDialog_assertIsNotDisplayed()
//    }
//
//    @Test
//    fun resetDialog_whenConfirmButtonIsClicked_localUserProgress_assertIsReset() = runTest {
//        screen.setLocalUserProgress(FUWOYA.id)
//        screen.setLocalUserLearningSetsCount(1)
//        screen.setIsTestUnlocked(true)
//        setContent()
//
//        screen.topAppBarMenu_click()
//        screen.drawerResetProgressButton_click()
//        screen.resetDialogConfirmButton_click()
//
//        val localUser = fakeUserRepository.observeLocalUser().first()
//        assertEquals(HIMIKASE.id, localUser.progress)
//        assertEquals(5, localUser.learningSetsCount)
//        assertEquals(false, localUser.isTestUnlocked)
//    }
//
//    fun setContent() {
//        composeTestRule.setContent {
//            MindlesslyHiraganaTheme {
//                Surface {
//                    HomeScreen(
//                        onNavigateToLearn = {},
//                        onNavigateToTest = {},
//                        viewModel = HomeViewModel(fakeUserRepository)
//                    )
//                }
//            }
//        }
//    }
//}
//
