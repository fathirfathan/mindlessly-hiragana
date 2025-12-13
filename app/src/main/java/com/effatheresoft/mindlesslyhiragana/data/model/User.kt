package com.effatheresoft.mindlesslyhiragana.data.model

data class User(
    val id: String,
    val progress: String,
    val learningSetsCount: Int,
    val isTestUnlocked: Boolean
)