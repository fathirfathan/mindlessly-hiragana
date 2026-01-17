package com.effatheresoft.mindlesslyhiragana.ui.quiz

import com.effatheresoft.mindlesslyhiragana.data.model.Hiragana

data class PossibleAnswer(
    val answer: Hiragana,
    val isCorrect: Boolean,
    val isSelected: Boolean
)