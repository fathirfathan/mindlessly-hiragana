package com.effatheresoft.mindlesslyhiragana.ui.testquiz

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.effatheresoft.mindlesslyhiragana.data.model.Hiragana
import com.effatheresoft.mindlesslyhiragana.data.model.HiraganaCategory
import com.effatheresoft.mindlesslyhiragana.data.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

data class QuestionState(
    val question: Hiragana,
    val answerAttempts: List<Hiragana> = emptyList()
)

data class TestQuizUiState(
    val loading: Boolean = false,
    val currentQuestion: Hiragana? = null,
    val remainingQuestionsCount: Int = -1,
)

@HiltViewModel
class TestQuizViewModel @Inject constructor(val userRepository: UserRepository): ViewModel() {
    private val _userProgress = userRepository.observeLocalUser().map { it.progress }
    private val _loading = MutableStateFlow(false)
    private val _questionStates = MutableStateFlow<List<QuestionState>>(emptyList())
    private val _currentQuestionIndex = MutableStateFlow(0)

    init {
        viewModelScope.launch {
            _userProgress.collect { progress ->
                val questions = HiraganaCategory.progressToCategoryList(progress).flatMap { it.hiraganaList }
                _questionStates.value = questions.map { question -> QuestionState(question = question) }
            }
        }
    }

    val uiState = combine(_loading, _questionStates, _currentQuestionIndex) { loading, questionStates, currentQuestionIndex ->
        TestQuizUiState(
            loading = loading,
            currentQuestion = questionStates.getOrNull(currentQuestionIndex)?.question,
            remainingQuestionsCount = questionStates.size - currentQuestionIndex - 1
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = TestQuizUiState(loading = true)
    )

    fun selectAnswer(hiragana: Hiragana, onAllQuestionsAnswered: (List<QuestionState>) -> Unit) = viewModelScope.launch {
        val currentQuestionState = _questionStates.value[_currentQuestionIndex.value]
        val isCurrentQuestionCorrect = hiragana == currentQuestionState.question
        val updatedQuestionState = currentQuestionState.copy(answerAttempts = currentQuestionState.answerAttempts + hiragana)
        _questionStates.value = _questionStates.value.toMutableList().apply {
            set(_currentQuestionIndex.value, updatedQuestionState)
        }

        if (isCurrentQuestionCorrect) {
            _currentQuestionIndex.value += 1
        }

        if (_currentQuestionIndex.value == _questionStates.value.size) {
            val isAllAnswersCorrect = _questionStates.value.run {
                count { it.answerAttempts.size == 1 } == size
            }

            if (isAllAnswersCorrect) {
                val nextHiraganaCategoryIndex = HiraganaCategory.entries.indexOfFirst { it.id == _userProgress.first() } + 1
                userRepository.updateLocalUserProgress(HiraganaCategory.entries[nextHiraganaCategoryIndex].id)
                userRepository.updateLocalUserIsTestUnlocked(false)
            }

            onAllQuestionsAnswered(_questionStates.value)
        }
    }
}