package com.effatheresoft.mindlesslyhiragana.ui.testresult

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.effatheresoft.mindlesslyhiragana.data.model.Hiragana
import com.effatheresoft.mindlesslyhiragana.data.model.HiraganaCategory
import com.effatheresoft.mindlesslyhiragana.data.repository.RefactoredQuizRepository
import com.effatheresoft.mindlesslyhiragana.data.repository.RefactoredUserRepository
import com.effatheresoft.mindlesslyhiragana.ui.testquiz.correctCounts
import com.effatheresoft.mindlesslyhiragana.ui.testquiz.incorrectCounts
import com.effatheresoft.mindlesslyhiragana.ui.testquiz.incorrectQuestions
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

data class TestResultUiState(
    val loading: Boolean = false,
    val canContinueLearning: Boolean = false,
    val progress: HiraganaCategory? = null,
    val correctCount: Int = -1,
    val incorrectCount: Int = -1,
    val incorrectHiraganaList: List<Hiragana> = emptyList()
)

@HiltViewModel
class TestResultViewModel @Inject constructor(
    userRepository: RefactoredUserRepository,
    quizRepository: RefactoredQuizRepository
): ViewModel() {
    private val _loading = MutableStateFlow(false)
    private val _observedLocalUser = userRepository.observeLocalUser()
    private val _observedQuizQuestions = quizRepository.observeQuizQuestions()

    val uiState = combine(_loading, _observedLocalUser, _observedQuizQuestions) { loading, localUser, observedQuizQuestions ->
        TestResultUiState(
            loading = loading,
            canContinueLearning = !localUser.isTestUnlocked,
            progress = localUser.progress,
            correctCount = observedQuizQuestions.correctCounts,
            incorrectCount = observedQuizQuestions.incorrectCounts,
            incorrectHiraganaList = observedQuizQuestions.incorrectQuestions.map { it.question }
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = TestResultUiState(loading = true)
    )
}