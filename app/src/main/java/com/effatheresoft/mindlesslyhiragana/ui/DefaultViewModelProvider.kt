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
import com.effatheresoft.mindlesslyhiragana.ui.results.QuizResult
import com.effatheresoft.mindlesslyhiragana.ui.results.ResultsViewModel
import kotlinx.serialization.json.Json

object DefaultViewModelProvider {
    val Factory: ViewModelProvider.Factory = viewModelFactory {
        initializer { HomeViewModel(application().userRepository) }
    }

    fun CreationExtras.application(): DefaultApplication {
        return (this[APPLICATION_KEY] as DefaultApplication)
    }

    fun getFactoryWithStringData(data: String) = viewModelFactory {
        initializer { ResultsViewModel(
            quizResults = Json.decodeFromString<List<QuizResult>>(data),
            userRepository = application().userRepository
        ) }
        initializer { DetailsViewModel(data, application().userRepository) }
        initializer { QuizViewModel(data, application().userRepository) }
    }
}