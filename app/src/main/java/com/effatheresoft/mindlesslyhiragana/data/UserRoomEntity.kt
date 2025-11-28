package com.effatheresoft.mindlesslyhiragana.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "user")
data class UserRoomEntity(
    @PrimaryKey val id: String,
    val progress: String,
)

fun UserRoomEntity.toUser(): User {
    return when (progress) {
        "1" -> User(
            id = id,
            progress = "himikase"
        )
        "2" -> User(
            id = id,
            progress = "fuwoya"
        )
        "3" -> User(
            id = id,
            progress = "ao"
        )
        "4" -> User(
            id = id,
            progress = "tsuune"
        )
        "5" -> User(
            id = id,
            progress = "kuherike"
        )
        "6" -> User(
            id = id,
            progress = "konitana"
        )
        "7" -> User(
            id = id,
            progress = "sumuroru"
        )
        "8" -> User(
            id = id,
            progress = "shiimo"
        )
        "9" -> User(
            id = id,
            progress = "toteso"
        )
        "10" -> User(
            id = id,
            progress = "wanere"
        )
        "11" -> User(
            id = id,
            progress = "noyumenu"
        )
        "12" -> User(
            id = id,
            progress = "yohamaho"
        )
        "13" -> User(
            id = id,
            progress = "sakichira"
        )
        else -> User(
            id = id,
            progress = "-1"
        )
    }
}