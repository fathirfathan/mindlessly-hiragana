package com.effatheresoft.mindlesslyhiragana.testquiz

import com.effatheresoft.mindlesslyhiragana.MainCoroutineRule
import com.effatheresoft.mindlesslyhiragana.data.FakeUserRepository
import com.effatheresoft.mindlesslyhiragana.data.model.Hiragana
import com.effatheresoft.mindlesslyhiragana.data.model.HiraganaCategory
import com.effatheresoft.mindlesslyhiragana.data.repository.UserRepository
import com.effatheresoft.mindlesslyhiragana.ui.testquiz.TestQuizViewModel
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class TestQuizViewModelTest {
    @OptIn(ExperimentalCoroutinesApi::class)
    @get:Rule
    val mainCoroutineRule = MainCoroutineRule()

    private lateinit var viewModel: TestQuizViewModel
    private lateinit var fakeUserRepository: UserRepository
    private suspend fun uiState() = viewModel.uiState.first()

    @Before
    fun init() = runTest {
        fakeUserRepository = FakeUserRepository()
        fakeUserRepository.updateLocalUserProgress(HiraganaCategory.HIMIKASE.id)

        viewModel = TestQuizViewModel(fakeUserRepository)
    }

    @Test
    fun currentQuestion_assertIsCorrect() = runTest {
        assertEquals(Hiragana.HI, uiState().currentQuestion)
    }

    @Test
    fun remainingQuestionsCount_assertIsCorrect() = runTest {
        assertEquals(3, uiState().remainingQuestionsCount)
    }
}