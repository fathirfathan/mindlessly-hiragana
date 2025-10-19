package com.effatheresoft.mindlesslyhiragana.data.local

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.effatheresoft.mindlesslyhiragana.data.User

@Entity(tableName = "user")
data class UserEntity(
    @PrimaryKey val id: String,
    @ColumnInfo(name = "highest_category_id") val highestCategoryId: String,
    @ColumnInfo(name = "learning_sets_count") val learningSetsCount: Int
)

fun UserEntity.toUser(): User {
    return User(id, highestCategoryId, learningSetsCount)
}