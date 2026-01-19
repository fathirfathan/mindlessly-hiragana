package com.effatheresoft.mindlesslyhiragana.data.model

data class User(
    val id: String,
    val highestCategory: HiraganaCategory,
    val repeatCategoryCount: Int,
    val isTestUnlocked: Boolean
)