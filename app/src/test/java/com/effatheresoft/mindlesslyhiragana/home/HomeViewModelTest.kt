package com.effatheresoft.mindlesslyhiragana.home

import androidx.compose.runtime.mutableStateOf
import com.effatheresoft.mindlesslyhiragana.data.User
import com.effatheresoft.mindlesslyhiragana.data.UserRepository
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.TestDispatcher
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestWatcher
import org.junit.runner.Description

@ExperimentalCoroutinesApi
class MainCoroutineRule(
    val testDispatcher: TestDispatcher = UnconfinedTestDispatcher()
) : TestWatcher() {

    override fun starting(description: Description?) {
        super.starting(description)
        Dispatchers.setMain(testDispatcher)
    }

    override fun finished(description: Description?) {
        super.finished(description)
        Dispatchers.resetMain()
    }
}

class FakeUserRepository : UserRepository {

    val localUser = mutableStateOf(User("localUser", "himikase"))

    override fun getLocalUser(): Flow<User> = flow {
        delay(1000)
        emit(localUser.value)
    }
}

@OptIn(ExperimentalCoroutinesApi::class)
class HomeViewModelTest {
    private lateinit var homeViewModel: HomeViewModel
    private lateinit var fakeUserRepository: FakeUserRepository
    private val localUserId = "localUser"

    @get:Rule
    val mainCoroutineRule = MainCoroutineRule()

    @Before
    fun setupViewModel() {
        fakeUserRepository = FakeUserRepository()
        homeViewModel = HomeViewModel(fakeUserRepository)
    }

    @Test
    fun uiStateReturnsLocalUser() = runTest {
        Dispatchers.setMain(StandardTestDispatcher())

        // Default user when user open the app
        val localUser = User(localUserId, "himikase")

        assertEquals(homeViewModel.uiState.first().isLoading, true)
        advanceUntilIdle()
        assertEquals(homeViewModel.uiState.first().isLoading, false)
        assertEquals(homeViewModel.uiState.first().progress, localUser.progress)
    }
}