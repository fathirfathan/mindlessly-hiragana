package com.effatheresoft.mindlesslyhiragana.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.effatheresoft.mindlesslyhiragana.Constants.DEFAULT_DATABASE_USER_TABLE_NAME
import com.effatheresoft.mindlesslyhiragana.data.model.HiraganaCategory
import com.effatheresoft.mindlesslyhiragana.data.model.User

@Entity(tableName = DEFAULT_DATABASE_USER_TABLE_NAME)
data class UserRoomEntity(
    @PrimaryKey val id: String,
    val progress: String,
    val learningSetsCount: Int,
    val isTestUnlocked: Boolean
)

fun UserRoomEntity.toUserOrNull(): User? = progress.toIntOrNull()?.let {
    return if (it in 1..HiraganaCategory.entries.size)
        User(
            id = id,
            progress = HiraganaCategory.entries[it - 1],
            learningSetsCount = learningSetsCount,
            isTestUnlocked = isTestUnlocked
        )
    else null
}

