package com.effatheresoft.mindlesslyhiragana.navigation
//
//import com.effatheresoft.mindlesslyhiragana.MainCoroutineRule
//import com.effatheresoft.mindlesslyhiragana.data.model.Hiragana
//import com.effatheresoft.mindlesslyhiragana.data.model.HiraganaCategory
//import com.effatheresoft.mindlesslyhiragana.ui.testquiz.QuestionState
//import junit.framework.TestCase.assertEquals
//import kotlinx.coroutines.ExperimentalCoroutinesApi
//import kotlinx.coroutines.flow.first
//import kotlinx.coroutines.test.runTest
//import org.junit.Before
//import org.junit.Rule
//import org.junit.Test
//
//class NavigationViewModelTest {
//    @OptIn(ExperimentalCoroutinesApi::class)
//    @get:Rule
//    val mainCoroutineRule: MainCoroutineRule = MainCoroutineRule()
//
//    private lateinit var viewModel: NavigationViewModel
//    private suspend fun uiState() = viewModel.uiState.first()
//
//    @Before
//    fun init() = runTest {
//        viewModel = NavigationViewModel()
//        viewModel.setQuestionStates(
//            HiraganaCategory.HIMIKASE.hiraganaList.map {
//                QuestionState(question = it, answerAttempts = listOf(it))
//            }
//        )
//    }
//
//    @Test
//    fun correctCount_assertIsCorrect() = runTest {
//        assertEquals(4, uiState().correctCount)
//    }
//
//    @Test
//    fun incorrectCount_assertIsCorrect() = runTest {
//        assertEquals(0, uiState().incorrectCount)
//    }
//
//    @Test
//    fun givenIncorrectQuestionState_incorrectHiraganaList_assertIsCorrect() = runTest {
//        viewModel.setQuestionStates(
//            HiraganaCategory.HIMIKASE.hiraganaList.map {
//                if (it == Hiragana.MI) {
//                    QuestionState(question = it, answerAttempts = listOf(Hiragana.HI, it))
//                } else {
//                    QuestionState(question = it, answerAttempts = listOf(it))
//                }
//            }
//        )
//
//        assertEquals(listOf(Hiragana.MI), uiState().incorrectHiraganaList)
//    }
//}