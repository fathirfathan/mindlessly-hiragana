package com.effatheresoft.mindlesslyhiragana.home

import com.effatheresoft.mindlesslyhiragana.data.model.HiraganaCategory
import com.effatheresoft.mindlesslyhiragana.data.repository.DefaultUserRepository
import com.effatheresoft.mindlesslyhiragana.data.repository.RefactoredQuizRepository
import com.effatheresoft.mindlesslyhiragana.data.repository.UserRepository
import com.effatheresoft.mindlesslyhiragana.sharedtest.data.FakeUserDao
import com.effatheresoft.mindlesslyhiragana.ui.home.HomeViewModel
import io.cucumber.java.After
import io.cucumber.java.Before
import io.cucumber.java.en.Given
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import io.cucumber.java.en.When
import kotlinx.coroutines.test.runTest
import io.cucumber.java.en.Then
import io.cucumber.java.en.And
import kotlinx.coroutines.flow.first

class HomeStepDefinitions {
    private lateinit var homeViewModel: HomeViewModel
    private lateinit var userRepository: UserRepository
    private lateinit var quizRepository: RefactoredQuizRepository
    private lateinit var fakeUserDao: FakeUserDao

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
        fakeUserDao = FakeUserDao()
        userRepository = DefaultUserRepository(fakeUserDao)
        quizRepository = RefactoredQuizRepository(fakeUserDao)
        homeViewModel = HomeViewModel(
            quizRepository = quizRepository,
            userRepository = userRepository
        )
    }

    @When("user progress is himikase")
    fun userProgressIsHimikase() = runTest {
        userRepository.updateLocalUserProgress(HiraganaCategory.HIMIKASE.id)
    }

    @Then("category up to `{}` is unlocked")
    fun categoryUpToCategoryIdIsUnlocked(categoryId: String) = runTest {
        val categoryTitle = HiraganaCategory.entries.first { it.id == categoryId }.kanaWithNakaguro
        val categories = homeViewModel.uiState.first().categories

        val targetCategoryIndex = categories.indexOfFirst { it.title == categoryTitle }
        for (i in 0..targetCategoryIndex) {
            assert(!categories[i].isLocked)
        }
    }

    @And("`{}` category is unlocked")
    fun certainCategoryIsUnlocked(categoryId: String) = runTest {
        val categories = homeViewModel.uiState.first().categories
        val targetCategory = categories.first { it.title == categoryId }
        assert(!targetCategory.isLocked)
    }

    @And("category after `{}` is locked")
    fun categoryAfterCertainCategoryIsLocked(categoryId: String) = runTest {
        val categoryTitle = HiraganaCategory.entries.first { it.id == categoryId }.kanaWithNakaguro
        val categories = homeViewModel.uiState.first().categories

        val targetCategoryIndex = categories.indexOfFirst { it.title == categoryTitle }
        for (i in targetCategoryIndex + 2..categories.lastIndex) {
            assert(categories[i].isLocked)
        }
    }
}