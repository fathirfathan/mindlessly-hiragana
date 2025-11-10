package com.effatheresoft.mindlesslyhiragana.ui.details

import androidx.lifecycle.ViewModel
import com.effatheresoft.mindlesslyhiragana.data.HiraganaRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update

class DetailsViewModel : ViewModel() {
    private val _id = MutableStateFlow("")
    private val _appBarTitle = MutableStateFlow("")

    fun setId(id: String) {
        _id.update { id }
    }

    fun getAppBarTitle(): StateFlow<String> {
        _appBarTitle.update {
            HiraganaRepository.getHiraganaCategoryById(_id.value)?.hiraganaRomaji ?: ""
        }
        return _appBarTitle
    }
}