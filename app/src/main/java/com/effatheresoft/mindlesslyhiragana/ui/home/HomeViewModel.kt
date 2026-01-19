package com.effatheresoft.mindlesslyhiragana.ui.home

import androidx.compose.material3.DrawerState
import androidx.compose.material3.DrawerValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.effatheresoft.mindlesslyhiragana.Constants.TEST_ALL_LEARNED_CATEGORY_ID
import com.effatheresoft.mindlesslyhiragana.Constants.TEST_ALL_LEARNED_CATEGORY_TITLE
import com.effatheresoft.mindlesslyhiragana.data.model.HiraganaCategory
import com.effatheresoft.mindlesslyhiragana.data.model.HiraganaCategory.HIMIKASE
import com.effatheresoft.mindlesslyhiragana.data.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

data class HomeUiState(
    val isLoading: Boolean = false,
    val categories: List<HomeCategory> = emptyList(),
    val isResetDialogOpen: Boolean = false
)

@HiltViewModel
class HomeViewModel @Inject constructor(val userRepository: UserRepository) : ViewModel() {

    private val isLoadingState = MutableStateFlow(false)
    private val observableUser = userRepository.observeUser()
    private val isResetDialogOpenState = MutableStateFlow(false)

    val uiState: StateFlow<HomeUiState> =
        combine(observableUser, isLoadingState, isResetDialogOpenState) {
            user, isLoading, isResetDialogOpen ->

            HomeUiState(
                isLoading = isLoading,
                categories = getHomeCategories(user.highestCategory),
                isResetDialogOpen = isResetDialogOpen
            )
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000L),
            initialValue = HomeUiState(isLoading = true)
        )

    private fun getHomeCategories(highestCategory: HiraganaCategory): List<HomeCategory> {
        val homeCategories = mutableListOf<HomeCategory>()
        val hiraganaCategories = HiraganaCategory.entries
        val testAllLearnedCategoryIndex = highestCategory.ordinal + 1

        val unlockedCategories = hiraganaCategories.take(testAllLearnedCategoryIndex)
            .map {
                HomeCategory(
                    id = it.id,
                    title = it.kanaWithNakaguro,
                    isLocked = false
                )
            }
        val testAllLearnedCategory = HomeCategory(
            id = TEST_ALL_LEARNED_CATEGORY_ID,
            title = TEST_ALL_LEARNED_CATEGORY_TITLE,
            isLocked = false
        )
        val lockedCategories = hiraganaCategories.drop(testAllLearnedCategoryIndex)
            .map {
                HomeCategory(
                    id = it.id,
                    title = it.kanaWithNakaguro,
                    isLocked = true
                )
            }

        homeCategories.addAll(unlockedCategories)
        homeCategories.add(testAllLearnedCategory)
        homeCategories.addAll(lockedCategories)

        return homeCategories
    }

    fun onDrawerToggled(drawerState: DrawerState, drawerScope: CoroutineScope) {
        drawerScope.launch {
            when (drawerState.currentValue) {
                DrawerValue.Closed -> drawerState.open()
                DrawerValue.Open -> drawerState.close()
            }
        }
    }

    fun onResetDialogOpen() {
        isResetDialogOpenState.value = true
    }

    fun onResetDialogDismiss() {
        isResetDialogOpenState.value = false
    }

    fun onResetDialogConfirm() = viewModelScope.launch {
        userRepository.updateHighestCategory(HIMIKASE)
        userRepository.updateRepeatCategoryCount(5)
        userRepository.updateIsTestUnlocked(false)

        onResetDialogDismiss()
    }
}