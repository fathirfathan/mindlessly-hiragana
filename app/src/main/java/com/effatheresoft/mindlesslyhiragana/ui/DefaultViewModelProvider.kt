package com.effatheresoft.mindlesslyhiragana.ui

import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewmodel.CreationExtras
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.effatheresoft.mindlesslyhiragana.DefaultApplication
import com.effatheresoft.mindlesslyhiragana.ui.details.DetailsViewModel
import com.effatheresoft.mindlesslyhiragana.ui.home.HomeViewModel
import com.effatheresoft.mindlesslyhiragana.ui.learntrain.QuizViewModel
import com.effatheresoft.mindlesslyhiragana.ui.results.ResultsViewModel

object DefaultViewModelProvider {
    val Factory: ViewModelProvider.Factory = viewModelFactory {
        initializer { HomeViewModel(application().hiraganaRepository) }
        initializer { DetailsViewModel(application().hiraganaRepository) }
        initializer { QuizViewModel(application().hiraganaRepository) }
        initializer { ResultsViewModel(application().hiraganaRepository) }
    }

    fun CreationExtras.application(): DefaultApplication {
        return (this[APPLICATION_KEY] as DefaultApplication)
    }
}