package com.effatheresoft.mindlesslyhiragana.home

import com.effatheresoft.mindlesslyhiragana.Constants.LOCAL_USER_ID
import com.effatheresoft.mindlesslyhiragana.MainCoroutineRule
import com.effatheresoft.mindlesslyhiragana.data.FakeUserRepository
import com.effatheresoft.mindlesslyhiragana.data.User
import com.effatheresoft.mindlesslyhiragana.data.UserRepository
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class HomeViewModelTest {
    private lateinit var homeViewModel: HomeViewModel
    private lateinit var fakeUserRepository: UserRepository

    @get:Rule
    val mainCoroutineRule = MainCoroutineRule()

    @Before
    fun setupViewModel() {
        fakeUserRepository = FakeUserRepository()
        homeViewModel = HomeViewModel(fakeUserRepository)
    }

    @Test
    fun uiState_containsLocalUser() = runTest {
        Dispatchers.setMain(StandardTestDispatcher())

        val localUser = User(LOCAL_USER_ID, "himikase", 5)
        fakeUserRepository.updateLocalUserProgress(localUser.progress)

        assertEquals(homeViewModel.uiState.first().isLoading, true)
        advanceUntilIdle()
        assertEquals(homeViewModel.uiState.first().isLoading, false)
        assertEquals(homeViewModel.uiState.first().progress, localUser.progress)
    }
}