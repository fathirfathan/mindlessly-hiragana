package com.effatheresoft.mindlesslyhiragana.ui.quiz

import com.effatheresoft.mindlesslyhiragana.data.model.Hiragana

data class Quiz(
    val question: Hiragana,
    val possibleAnswers: List<PossibleAnswer>
)

val Quiz.isCorrect
    get() = this.possibleAnswers.count { it.isSelected } == 1
val List<Quiz>.correctCounts
    get() = count { quiz -> quiz.isCorrect }
val List<Quiz>.incorrectCounts
    get() = count { quiz -> !quiz.isCorrect }