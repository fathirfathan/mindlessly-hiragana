package com.effatheresoft.mindlesslyhiragana.learn

import com.effatheresoft.mindlesslyhiragana.Constants.DEFAULT_LEARNING_SETS_COUNT
import com.effatheresoft.mindlesslyhiragana.Constants.LOCAL_USER_ID
import com.effatheresoft.mindlesslyhiragana.MainCoroutineRule
import com.effatheresoft.mindlesslyhiragana.data.FakeUserRepository
import com.effatheresoft.mindlesslyhiragana.data.HiraganaCategory.HIMIKASE
import com.effatheresoft.mindlesslyhiragana.data.User
import com.effatheresoft.mindlesslyhiragana.data.UserRepository
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class LearnViewModelTest {
    private lateinit var fakeUserRepository: UserRepository
    private lateinit var learnViewModel: LearnViewModel

    @OptIn(ExperimentalCoroutinesApi::class)
    @get:Rule
    val mainCoroutineRule = MainCoroutineRule()

    @Before
    fun setupViewModel() {
        fakeUserRepository = FakeUserRepository()
        learnViewModel = LearnViewModel(fakeUserRepository)
    }

    @Test
    fun uiState_containsLearningSetsCount() = runTest {
        val localUser = User(LOCAL_USER_ID, HIMIKASE.id, learningSetsCount = DEFAULT_LEARNING_SETS_COUNT)
        fakeUserRepository.updateLocalUserLearningSetsCount(localUser.learningSetsCount)

        assertEquals(learnViewModel.uiState.first().learningSetsCount, localUser.learningSetsCount)
    }
}