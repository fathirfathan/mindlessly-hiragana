package com.effatheresoft.mindlesslyhiragana.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.effatheresoft.mindlesslyhiragana.Constants.DEFAULT_DATABASE_USER_TABLE_NAME
import com.effatheresoft.mindlesslyhiragana.Constants.DEFAULT_HIGHEST_CATEGORY
import com.effatheresoft.mindlesslyhiragana.Constants.DEFAULT_REPEAT_CATEGORY_COUNT
import com.effatheresoft.mindlesslyhiragana.Constants.LOCAL_USER_ID
import com.effatheresoft.mindlesslyhiragana.data.model.HiraganaCategory
import com.effatheresoft.mindlesslyhiragana.data.model.User

@Entity(tableName = DEFAULT_DATABASE_USER_TABLE_NAME)
data class LocalUserRoomEntity(
    @PrimaryKey val id: String = LOCAL_USER_ID,
    val highestCategory: String,
    val repeatCategoryCount: Int,
    val isTestUnlocked: Boolean
) {
    companion object {
        val default get() = LocalUserRoomEntity(
            id = LOCAL_USER_ID,
            highestCategory = DEFAULT_HIGHEST_CATEGORY,
            repeatCategoryCount = DEFAULT_REPEAT_CATEGORY_COUNT,
            isTestUnlocked = false
        )
    }
}

fun LocalUserRoomEntity.toUserOrNull(): User? = highestCategory.toIntOrNull()?.let {
    return if (it in 1..HiraganaCategory.entries.size)
        User(
            id = id,
            highestCategory = HiraganaCategory.entries[it - 1],
            repeatCategoryCount = repeatCategoryCount,
            isTestUnlocked = isTestUnlocked
        )
    else null
}

