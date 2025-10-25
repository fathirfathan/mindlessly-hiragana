package com.effatheresoft.mindlesslyhiragana.data.local

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.effatheresoft.mindlesslyhiragana.data.User
import com.effatheresoft.mindlesslyhiragana.data.UserInteraction

@Entity(tableName = "user")
@TypeConverters(Converters::class)
data class UserEntity(
    @PrimaryKey val id: String,
    @ColumnInfo(name = "highest_category_id") val highestCategoryId: String,
    @ColumnInfo(name = "learning_sets_count") val learningSetsCount: Int,
    @ColumnInfo(name = "user_interactions") val userInteractions: List<UserInteraction> = emptyList()
)

fun UserEntity.toUser(): User {
    return User(id, highestCategoryId, learningSetsCount, userInteractions)
}
