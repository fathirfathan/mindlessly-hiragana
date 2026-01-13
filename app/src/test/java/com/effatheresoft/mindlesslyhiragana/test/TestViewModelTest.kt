package com.effatheresoft.mindlesslyhiragana.test
//
//import com.effatheresoft.mindlesslyhiragana.MainCoroutineRule
//import com.effatheresoft.mindlesslyhiragana.data.FakeUserRepository
//import com.effatheresoft.mindlesslyhiragana.data.model.HiraganaCategory.HIMIKASE
//import com.effatheresoft.mindlesslyhiragana.data.model.HiraganaCategory.FUWOYA
//import com.effatheresoft.mindlesslyhiragana.data.repository.UserRepository
//import com.effatheresoft.mindlesslyhiragana.ui.test.TestViewModel
//import junit.framework.TestCase.assertEquals
//import kotlinx.coroutines.ExperimentalCoroutinesApi
//import kotlinx.coroutines.flow.first
//import kotlinx.coroutines.test.runTest
//import org.junit.Before
//import org.junit.Rule
//import org.junit.Test
//
//class TestViewModelTest {
//    private lateinit var viewModel: TestViewModel
//    private lateinit var userRepository: UserRepository
//
//    @OptIn(ExperimentalCoroutinesApi::class)
//    @get:Rule
//    val mainCoroutineRule = MainCoroutineRule()
//
//    @Before
//    fun setupViewModel() {
//        userRepository = FakeUserRepository()
//        viewModel = TestViewModel(userRepository)
//    }
//
//    @Test
//    fun isTestUnlocked_assertInUiState() = runTest {
//        assertEquals(false, viewModel.uiState.first().isTestUnlocked)
//    }
//
//    @Test
//    fun givenUserProgress_categoryList_assertCorrect() = runTest {
//        val categoryList = listOf(HIMIKASE, FUWOYA)
//        userRepository.updateLocalUserProgress(FUWOYA.id)
//
//        assertEquals(categoryList, viewModel.uiState.first().categoryList)
//    }
//}