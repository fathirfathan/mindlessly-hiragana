package com.effatheresoft.mindlesslyhiragana.ui.testquiz

import com.effatheresoft.mindlesslyhiragana.data.model.Hiragana

data class QuizQuestion(
    val question: Hiragana,
    val answerAttempts: List<Hiragana> = emptyList()
) {
    val isCorrect: Boolean get() = answerAttempts.size == 1 && answerAttempts.first() == question
    val isAnswered: Boolean get() = answerAttempts.lastOrNull()?.let { it == question } ?: false
}

val List<QuizQuestion>.correctCounts: Int get() = count { it.isCorrect }
val List<QuizQuestion>.incorrectCounts: Int get() = count { !it.isCorrect }
val List<QuizQuestion>.incorrectQuestions: List<QuizQuestion> get() = filter { !it.isCorrect }
val List<QuizQuestion>.isAllQuestionsAnswered: Boolean get() = lastOrNull()?.isAnswered ?: false
val List<QuizQuestion>.isAllQuestionsCorrect: Boolean get() = all { it.isCorrect }