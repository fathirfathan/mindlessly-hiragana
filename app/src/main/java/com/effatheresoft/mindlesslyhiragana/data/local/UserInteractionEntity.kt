package com.effatheresoft.mindlesslyhiragana.data.local

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import com.effatheresoft.mindlesslyhiragana.data.UserInteraction

@Entity(
    tableName = "user_interaction",
    foreignKeys = [
        ForeignKey(
            entity = UserEntity::class,
            parentColumns = ["id"],
            childColumns = ["user_id"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class UserInteractionEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    @ColumnInfo(name = "user_id", index = true) val userId: String,
    val timestamp: String,
    val event: String,
    val target: String
)

fun UserInteractionEntity.toUserInteraction(): UserInteraction {
    return UserInteraction(timestamp, event, target)
}

fun UserInteraction.toUserInteractionEntity(userId: String): UserInteractionEntity {
    return UserInteractionEntity(userId = userId, timestamp = timestamp, event = event, target = target)
}
