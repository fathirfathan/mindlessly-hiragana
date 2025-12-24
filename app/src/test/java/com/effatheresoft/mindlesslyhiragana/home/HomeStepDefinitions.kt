package com.effatheresoft.mindlesslyhiragana.home

import com.effatheresoft.mindlesslyhiragana.data.FakeUserRepository
import com.effatheresoft.mindlesslyhiragana.data.repository.UserRepository
import com.effatheresoft.mindlesslyhiragana.ui.home.HomeViewModel
import io.cucumber.java.After
import io.cucumber.java.Before
import io.cucumber.java.en.Given
import io.cucumber.java.en.Then
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain

class HomeStepDefinitions {
    private lateinit var homeViewModel: HomeViewModel
    private lateinit var fakeUserRepository: UserRepository

    @OptIn(ExperimentalCoroutinesApi::class)
    @Before
    fun setup() {
        Dispatchers.setMain(UnconfinedTestDispatcher())
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @After
    fun teardown() {
        Dispatchers.resetMain()
    }

    @Given("viewmodel is set up")
    fun viewmodelSetup() {
        fakeUserRepository = FakeUserRepository()
        homeViewModel = HomeViewModel(fakeUserRepository)
    }

    @Then("progress is himikase")
    fun progressIsHimikase() {
        assertEquals("himikase", homeViewModel.uiState.value.progress)
    }
}