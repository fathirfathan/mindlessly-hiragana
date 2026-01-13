package com.effatheresoft.mindlesslyhiragana.ui.learn
//
//import androidx.compose.material3.Surface
//import androidx.compose.ui.semantics.ProgressBarRangeInfo
//import androidx.compose.ui.semantics.Role
//import androidx.compose.ui.semantics.SemanticsActions
//import androidx.compose.ui.semantics.SemanticsProperties
//import androidx.compose.ui.test.SemanticsMatcher
//import androidx.compose.ui.test.assertIsDisplayed
//import androidx.compose.ui.test.hasProgressBarRangeInfo
//import androidx.compose.ui.test.hasText
//import androidx.compose.ui.test.junit4.createAndroidComposeRule
//import androidx.compose.ui.test.onAllNodesWithText
//import androidx.compose.ui.test.onFirst
//import androidx.compose.ui.test.onNodeWithContentDescription
//import androidx.compose.ui.test.onNodeWithText
//import androidx.compose.ui.test.performSemanticsAction
//import androidx.test.ext.junit.runners.AndroidJUnit4
//import com.effatheresoft.mindlesslyhiragana.Constants.DEFAULT_LEARNING_SETS_COUNT
//import com.effatheresoft.mindlesslyhiragana.HiltTestActivity
//import com.effatheresoft.mindlesslyhiragana.R
//import com.effatheresoft.mindlesslyhiragana.data.model.HiraganaCategory.HIMIKASE
//import com.effatheresoft.mindlesslyhiragana.ui.theme.MindlesslyHiraganaTheme
//import dagger.hilt.android.testing.HiltAndroidRule
//import dagger.hilt.android.testing.HiltAndroidTest
//import org.junit.Before
//import org.junit.Rule
//import org.junit.Test
//import org.junit.runner.RunWith
//
//@RunWith(AndroidJUnit4::class)
//@HiltAndroidTest
//class LearnScreenTest {
//    @get:Rule(order = 0)
//    var hiltRule = HiltAndroidRule(this)
//
//    @get:Rule(order = 1)
//    val composeTestRule = createAndroidComposeRule<HiltTestActivity>()
//    private val activity get() = composeTestRule.activity
//
//    @Before
//    fun init() {
//        hiltRule.inject()
//    }
//
//    @Test
//    fun topAppBar_assertIsDisplayed() {
//        setContent()
//
//        composeTestRule.onAllNodesWithText(activity.getString(R.string.learn)).onFirst().assertIsDisplayed()
//        composeTestRule.onNodeWithContentDescription(activity.getString(R.string.navigate_back)).assertIsDisplayed()
//    }
//
//    @Test
//    fun hiraganaCategory_assertIsDisplayed() {
//        setContent()
//
//        composeTestRule.onNodeWithText(HIMIKASE.kanaWithNakaguro).assertIsDisplayed()
//    }
//
//    @Test
//    fun learningSetsCustomizer_withDefaultSetsCount_assertIsDisplayed() {
//        setContent()
//
//        composeTestRule.onNodeWithText(activity.getString(R.string.learning_sets_n_sets, DEFAULT_LEARNING_SETS_COUNT)).assertIsDisplayed()
//        composeTestRule.onNode(
//            hasProgressBarRangeInfo(
//                ProgressBarRangeInfo(
//                    current = DEFAULT_LEARNING_SETS_COUNT.toFloat(),
//                    range = 1f..10f,
//                    steps = 8
//                )
//            )
//        ).assertIsDisplayed()
//    }
//
//    @Test
//    fun learningSetsCustomizer_whenCountIsSetToOne_assertIsDisplayed() {
//        val currentCount = DEFAULT_LEARNING_SETS_COUNT
//        val changedCount = when {
//            DEFAULT_LEARNING_SETS_COUNT >= 10 -> 9
//            else -> DEFAULT_LEARNING_SETS_COUNT + 1
//        }
//        setContent()
//
//        composeTestRule.onNode(
//            hasProgressBarRangeInfo(
//                ProgressBarRangeInfo(
//                    current = currentCount.toFloat(),
//                    range = 1f..10f,
//                    steps = 8
//                )
//            )
//        ).performSemanticsAction(SemanticsActions.SetProgress) { it(changedCount.toFloat()) }
//
//        composeTestRule.onNode(
//            hasProgressBarRangeInfo(
//                ProgressBarRangeInfo(
//                    current = changedCount.toFloat(),
//                    range = 1f..10f,
//                    steps = 8
//                )
//            )
//        ).assertIsDisplayed()
//        composeTestRule.onNodeWithText(activity.getString(R.string.learning_sets_n_sets, changedCount)).assertIsDisplayed()
//    }
//
//    @Test
//    fun learnButton_assertIsDisplayed() {
//        setContent()
//
//        composeTestRule.onNode(isButton() and hasText(activity.getString(R.string.learn))).assertIsDisplayed()
//    }
//
//    fun setContent() {
//        composeTestRule.setContent {
//            MindlesslyHiraganaTheme {
//                Surface {
//                    LearnScreen(
//                        categoryId = HIMIKASE.id,
//                        onNavigationIconClick = {},
//                        onLearnButtonClick = {})
//                }
//            }
//        }
//    }
//
//}
//
//fun isButton() = SemanticsMatcher.expectValue(SemanticsProperties.Role, Role.Button)
