package com.effatheresoft.mindlesslyhiragana.testresult

import com.effatheresoft.mindlesslyhiragana.MainCoroutineRule
import com.effatheresoft.mindlesslyhiragana.data.FakeUserRepository
import com.effatheresoft.mindlesslyhiragana.data.model.HiraganaCategory
import com.effatheresoft.mindlesslyhiragana.data.repository.UserRepository
import com.effatheresoft.mindlesslyhiragana.ui.testresult.TestResultViewModel
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class TestResultViewModelTest {
    @OptIn(ExperimentalCoroutinesApi::class)
    @get:Rule
    val mainCoroutineRule = MainCoroutineRule()

    private lateinit var fakeUserRepository: UserRepository
    private lateinit var viewModel: TestResultViewModel
    private suspend fun uiState() = viewModel.uiState.first()

    @Before
    fun init() = runTest {
        fakeUserRepository = FakeUserRepository()

        fakeUserRepository.updateLocalUserProgress(HiraganaCategory.HIMIKASE.id)
        fakeUserRepository.updateLocalUserIsTestUnlocked(false)

        viewModel = TestResultViewModel(fakeUserRepository)
    }

    @Test
    fun whenIsTestUnlockedTrue_canContinueLearning_assertIsFalse() = runTest {
        fakeUserRepository.updateLocalUserIsTestUnlocked(true)
        assertEquals(false, uiState().canContinueLearning)
    }

    @Test
    fun whenIsTestUnlockedFalse_canContinueLearning_assertIsTrue() = runTest {
        fakeUserRepository.updateLocalUserIsTestUnlocked(false)
        assertEquals(true, uiState().canContinueLearning)
    }
}