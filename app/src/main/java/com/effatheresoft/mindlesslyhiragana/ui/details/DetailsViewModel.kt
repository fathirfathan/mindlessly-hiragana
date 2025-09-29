package com.effatheresoft.mindlesslyhiragana.ui.details

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.effatheresoft.mindlesslyhiragana.data.Hiragana
import com.effatheresoft.mindlesslyhiragana.data.HiraganaRepository

data class DetailsUiState(
    val appBarTitle: String = ""
)

class DetailsViewModel : ViewModel() {
    private var id = ""
    private var appBarTitle by mutableStateOf("")

    private var hiraganaList by mutableStateOf(listOf<Hiragana>())
    var uiState by mutableStateOf(DetailsUiState())
        private set

    fun initializeWithId(id: String) {
        this.id = id
        hiraganaList = HiraganaRepository.getHiraganaCategoryById(id).hiraganaList

        val hiraganaString = hiraganaList.joinToString("") { it.hiragana }
        val romajiString = hiraganaList.joinToString(" ") { it.romaji.uppercase() }
        appBarTitle = "$hiraganaString $romajiString"

        uiState = uiState.copy(
            appBarTitle = appBarTitle
        )
    }
}