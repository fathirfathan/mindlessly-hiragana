package com.effatheresoft.mindlesslyhiragana.result
//
//import com.effatheresoft.mindlesslyhiragana.MainCoroutineRule
//import com.effatheresoft.mindlesslyhiragana.data.FakeUserRepository
//import com.effatheresoft.mindlesslyhiragana.data.model.Hiragana.HI
//import com.effatheresoft.mindlesslyhiragana.data.model.Hiragana.KA
//import com.effatheresoft.mindlesslyhiragana.data.model.Hiragana.MI
//import com.effatheresoft.mindlesslyhiragana.data.model.Hiragana.SE
//import com.effatheresoft.mindlesslyhiragana.data.repository.QuizRepository
//import com.effatheresoft.mindlesslyhiragana.data.repository.UserRepository
//import com.effatheresoft.mindlesslyhiragana.sharedtest.data.FakeQuizRepository
//import com.effatheresoft.mindlesslyhiragana.ui.result.ResultViewModel
//import junit.framework.TestCase.assertEquals
//import kotlinx.coroutines.ExperimentalCoroutinesApi
//import kotlinx.coroutines.flow.first
//import kotlinx.coroutines.test.runTest
//import org.junit.Before
//import org.junit.Rule
//import org.junit.Test
//
//class ResultViewModelTest {
//    private lateinit var viewModel: ResultViewModel
//    private lateinit var fakeQuizRepository: QuizRepository
//    private lateinit var fakeUserRepository: UserRepository
//
//    @OptIn(ExperimentalCoroutinesApi::class)
//    @get:Rule
//    val mainCoroutineRule = MainCoroutineRule()
//
//    @Before
//    fun setupViewModel() {
//        fakeQuizRepository = FakeQuizRepository(isPrepopulated = true)
//        fakeUserRepository = FakeUserRepository()
//        viewModel = ResultViewModel(
//            quizRepository = fakeQuizRepository,
//            userRepository = fakeUserRepository
//        )
//    }
//
//    @Test
//    fun withPrepopulatedQuizzes_countStates_assertAreCorrect() = runTest {
//        val expectedIndividualIncorrectCounts = listOf(
//            HI to 0,
//            MI to 2,
//            KA to 0,
//            SE to 2
//        )
//
//        assertEquals(4, viewModel.uiState.first().correctCounts)
//        assertEquals(4, viewModel.uiState.first().incorrectCounts)
//        viewModel.uiState.first().individualIncorrectCounts.forEachIndexed { index, pair ->
//            assertEquals(expectedIndividualIncorrectCounts[index], pair)
//        }
//    }
//
//    @Test
//    fun isTestUnlocked_assertInUiState() = runTest {
//        val isTestUnlocked = false
//        fakeUserRepository.updateLocalUserIsTestUnlocked(isTestUnlocked)
//        assertEquals(isTestUnlocked, viewModel.uiState.first().isTestUnlocked)
//    }
//}
//
