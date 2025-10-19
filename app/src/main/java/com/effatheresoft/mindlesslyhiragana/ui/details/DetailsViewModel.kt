package com.effatheresoft.mindlesslyhiragana.ui.details

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.effatheresoft.mindlesslyhiragana.data.Hiragana
import com.effatheresoft.mindlesslyhiragana.data.User
import com.effatheresoft.mindlesslyhiragana.data.UserRepository
import com.effatheresoft.mindlesslyhiragana.data.getCategoryById
import com.effatheresoft.mindlesslyhiragana.util.Result
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

class DetailsViewModel(
    private val categoryId: String,
    private val userRepository: UserRepository
) : ViewModel() {
    private var user: User? = null
    private var appBarTitle = ""
    private var learningSetsCount = 3
    private val _uiState = MutableStateFlow<DetailsUiState>(DetailsUiState.Loading)

    val uiState = _uiState.asStateFlow()
    init {
        userRepository.getDefaultUser().onEach {
            when (it) {
                is Result.Success -> {
                    it.data?.let { user ->
                        this.user = user
                        appBarTitle = getAppBarTitle(categoryId)
                        learningSetsCount = user.learningSetsCount
                        setUiStateSuccess()
                    }
                }
                is Result.Loading -> {}
                is Result.Error -> { _uiState.value = DetailsUiState.Error(it.exception) }
            }
        }.launchIn(viewModelScope)
    }

    fun getAppBarTitle(categoryId: String): String {
        val hiraganaCategory = Hiragana.categories.getCategoryById(categoryId)
        val hiraganaString =
            hiraganaCategory?.hiraganaList?.joinToString("") { it.hiragana } ?: ""
        val romajiString =
            hiraganaCategory?.hiraganaList?.joinToString(" ") { it.romaji.uppercase() } ?: ""
        return "$hiraganaString $romajiString"
    }

    fun onLearningSetsCountChange(learningSetsCount: Int) {
        this.learningSetsCount = learningSetsCount
        setUiStateSuccess()
    }

    fun setUiStateSuccess() {
        _uiState.value = DetailsUiState.Success(
            appBarTitle = appBarTitle,
            learningSetsCount = learningSetsCount
        )
    }

    fun onNavigateToLearn( onNavigateToLearn: (Int) -> Unit ) {
        user?.let { user ->
            userRepository.updateUser(user.copy(learningSetsCount = learningSetsCount)).onEach {
                when(it) {
                    is Result.Success -> {
                        setUiStateSuccess()
                        onNavigateToLearn(learningSetsCount)
                    }
                    is Result.Loading -> { _uiState.value = DetailsUiState.Loading }
                    is Result.Error -> {
                        _uiState.value = DetailsUiState.Error(it.exception)
                    }
                }
            }.launchIn(viewModelScope)
        }
    }
}

sealed class DetailsUiState {
    data object Loading: DetailsUiState()
    data class Success(
        val appBarTitle: String,
        val learningSetsCount: Int,
    ): DetailsUiState()
    data class Error(val exception: Throwable): DetailsUiState()
}
