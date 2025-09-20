package com.effatheresoft.mindlesslyhiragana.ui.home

import androidx.lifecycle.ViewModel
import com.effatheresoft.mindlesslyhiragana.data.HiraganaCategory
import com.effatheresoft.mindlesslyhiragana.data.HiraganaRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class HomeViewModel: ViewModel() {
    val hiraganaCategories: StateFlow<List<HiraganaCategory>> =
        MutableStateFlow(HiraganaRepository.getHiraganaCategories())
}