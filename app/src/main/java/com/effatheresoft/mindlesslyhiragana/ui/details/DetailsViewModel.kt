package com.effatheresoft.mindlesslyhiragana.ui.details

import androidx.lifecycle.ViewModel
import com.effatheresoft.mindlesslyhiragana.data.Hiragana
import com.effatheresoft.mindlesslyhiragana.data.UserRepository
import com.effatheresoft.mindlesslyhiragana.data.getCategoryById
import com.effatheresoft.mindlesslyhiragana.ui.learntrain.QuizUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class DetailsViewModel(
    private val categoryId: String,
    private val userRepository: UserRepository
) : ViewModel() {
    private val user = userRepository.getDefaultUser()
    private var appBarTitle = ""
    private var learningSetsCount = 3
    private val _uiState = MutableStateFlow<DetailsUiState>(DetailsUiState.Loading)

    val uiState = _uiState.asStateFlow()
    init {
        appBarTitle = getAppBarTitle(categoryId)
        setUiStateSuccess()
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
}

sealed class DetailsUiState {
    data object Loading: DetailsUiState()
    data class Success(
        val appBarTitle: String,
        val learningSetsCount: Int,
    ): DetailsUiState()
    data class Error(val exception: Throwable): DetailsUiState()
}
