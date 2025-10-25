package com.effatheresoft.mindlesslyhiragana.data.local

import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.Relation
import com.effatheresoft.mindlesslyhiragana.data.User

@Entity(tableName = "user")
data class UserEntity(
    @PrimaryKey val id: String,
    @ColumnInfo(name = "highest_category_id") val highestCategoryId: String,
    @ColumnInfo(name = "learning_sets_count") val learningSetsCount: Int
)

data class UserWithInteractions(
    @Embedded val user: UserEntity,
    @Relation(
        parentColumn = "id",
        entityColumn = "user_id"
    )
    val interactions: List<UserInteractionEntity>
)

fun UserWithInteractions.toUser(): User {
    return User(
        id = user.id,
        highestCategoryId = user.highestCategoryId,
        learningSetsCount = user.learningSetsCount,
        userInteractions = interactions.map { it.toUserInteraction() }
    )
}
