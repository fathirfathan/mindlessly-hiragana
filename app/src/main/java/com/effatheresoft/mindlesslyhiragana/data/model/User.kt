package com.effatheresoft.mindlesslyhiragana.data.model

data class User(
    val id: String,
    val progress: HiraganaCategory,
    val learningSetsCount: Int,
    val isTestUnlocked: Boolean
)