package com.kaishijak.mindlesslyhiragana.ui.result

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kaishijak.mindlesslyhiragana.data.model.Hiragana
import com.kaishijak.mindlesslyhiragana.data.repository.QuizRepository
import com.kaishijak.mindlesslyhiragana.data.repository.UserRepository
import com.kaishijak.mindlesslyhiragana.ui.testquiz.correctCounts
import com.kaishijak.mindlesslyhiragana.ui.testquiz.incorrectCounts
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

data class ResultUiState(
    val isLoading: Boolean = false,
    val correctCounts: Int? = null,
    val incorrectCounts: Int? = null,
    val individualIncorrectCounts: List<Pair<Hiragana, Int>> = emptyList(),
    val isTestUnlocked: Boolean = false
)

@HiltViewModel
class ResultViewModel @Inject constructor(
    quizRepository: QuizRepository,
    userRepository: UserRepository
) : ViewModel() {

    private val _isLoading = MutableStateFlow(false)
    private val observableQuizQuestions = quizRepository.observeQuizQuestions()
    private val observableIsTestUnlocked = userRepository.observeUser().map { it.isTestUnlocked }

    val uiState = combine(
        _isLoading,
        observableQuizQuestions,
        observableIsTestUnlocked
    ) { isLoading, quizQuestions, isTestUnlocked ->

        val individualIncorrectCounts = quizQuestions.groupBy { it.question }.map {
            (hiragana, groupedQuizQuestions) ->

            hiragana to groupedQuizQuestions.count { question -> !question.isCorrect }
        }
        ResultUiState(
            isLoading = isLoading,
            correctCounts = quizQuestions.correctCounts,
            incorrectCounts = quizQuestions.incorrectCounts,
            individualIncorrectCounts = individualIncorrectCounts,
            isTestUnlocked = isTestUnlocked
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = ResultUiState(isLoading = false)
    )
}