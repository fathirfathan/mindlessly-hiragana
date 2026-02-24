package com.kaishijak.mindlesslyhiragana.ui.testresult

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kaishijak.mindlesslyhiragana.data.model.Hiragana
import com.kaishijak.mindlesslyhiragana.data.model.HiraganaCategory
import com.kaishijak.mindlesslyhiragana.data.repository.QuizRepository
import com.kaishijak.mindlesslyhiragana.data.repository.UserRepository
import com.kaishijak.mindlesslyhiragana.ui.testquiz.correctCounts
import com.kaishijak.mindlesslyhiragana.ui.testquiz.incorrectCounts
import com.kaishijak.mindlesslyhiragana.ui.testquiz.incorrectQuestions
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
    val correctCount: Int? = null,
    val incorrectCount: Int? = null,
    val incorrectHiraganaList: List<Hiragana> = emptyList()
)

@HiltViewModel
class TestResultViewModel @Inject constructor(
    userRepository: UserRepository,
    quizRepository: QuizRepository
): ViewModel() {
    private val _loading = MutableStateFlow(false)
    private val observableUser = userRepository.observeUser()
    private val quizQuestionsState = quizRepository.observeQuizQuestions()

    val uiState = combine(_loading, observableUser, quizQuestionsState) {
        loading, localUser, observedQuizQuestions ->

        TestResultUiState(
            loading = loading,
            canContinueLearning = !localUser.isTestUnlocked,
            progress = localUser.highestCategory,
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