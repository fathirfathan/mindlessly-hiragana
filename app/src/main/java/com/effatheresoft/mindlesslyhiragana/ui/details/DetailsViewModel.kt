package com.effatheresoft.mindlesslyhiragana.ui.details

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.effatheresoft.mindlesslyhiragana.data.Hiragana
import com.effatheresoft.mindlesslyhiragana.data.HiraganaCategory
import com.effatheresoft.mindlesslyhiragana.data.HiraganaRepository
import com.effatheresoft.mindlesslyhiragana.util.Result
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

sealed class DetailsUiState {
    data object Loading: DetailsUiState()
    data class Success(val appBarTitle: String): DetailsUiState()
    data class Error(val exception: Throwable): DetailsUiState()
}

class DetailsViewModel(
    private val repository: HiraganaRepository
) : ViewModel() {
    private var appBarTitle = ""
    private var hiraganaList = listOf<Hiragana>()
    var id = ""

    private lateinit var hiraganaCategory: Flow<Result<HiraganaCategory>>

    private val _uiState = MutableStateFlow<DetailsUiState>(DetailsUiState.Loading)
    val uiState = _uiState.asStateFlow()

    fun initializeWithId(id: String) {
        if (id == this.id) return

        this.id = id
        hiraganaCategory = repository.getHiraganaCategoryById(id)

        hiraganaCategory.onEach {
            when (it) {
                is Result.Loading -> _uiState.value = DetailsUiState.Loading
                is Result.Success -> {
                    hiraganaList = it.data.hiraganaList
                    setAppBarTitle()
                }
                is Result.Error -> {
                    _uiState.value = DetailsUiState.Error(it.exception)
                    Log.d("DetailsViewModel", "Error: ${it.exception}")
                }
            }
        }.launchIn(viewModelScope)
    }

    fun setAppBarTitle() {
        val hiraganaString = hiraganaList.joinToString("") { it.hiragana }
        val romajiString = hiraganaList.joinToString(" ") { it.romaji.uppercase() }
        appBarTitle = "$hiraganaString $romajiString"
        _uiState.value = DetailsUiState.Success(appBarTitle)
    }
}