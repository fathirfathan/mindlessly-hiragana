package com.effatheresoft.mindlesslyhiragana.ui.details

import androidx.lifecycle.ViewModel
import com.effatheresoft.mindlesslyhiragana.data.Hiragana
import com.effatheresoft.mindlesslyhiragana.data.UserRepository
import com.effatheresoft.mindlesslyhiragana.data.getCategoryById
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

sealed class DetailsUiState {
    data object Loading: DetailsUiState()
    data class Success(val appBarTitle: String): DetailsUiState()
    data class Error(val exception: Throwable): DetailsUiState()
}

class DetailsViewModel(
    private val categoryId: String,
    private val userRepository: UserRepository
) : ViewModel() {
    private val user = userRepository.getDefaultUser()

    private val _uiState = MutableStateFlow<DetailsUiState>(DetailsUiState.Loading)
    val uiState = _uiState.asStateFlow()

    init {
        _uiState.value = DetailsUiState.Success(getAppBarTitle(categoryId))
    }

    fun getAppBarTitle(categoryId: String): String {
        val hiraganaCategory = Hiragana.categories.getCategoryById(categoryId)
        val hiraganaString =
            hiraganaCategory?.hiraganaList?.joinToString("") { it.hiragana } ?: ""
        val romajiString =
            hiraganaCategory?.hiraganaList?.joinToString(" ") { it.romaji.uppercase() } ?: ""
        return "$hiraganaString $romajiString"
    }
}