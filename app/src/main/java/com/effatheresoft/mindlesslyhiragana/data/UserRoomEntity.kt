package com.effatheresoft.mindlesslyhiragana.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "user")
data class UserRoomEntity(
    @PrimaryKey val id: String,
    val progress: String,
    val learningSetsCount: Int
)

fun UserRoomEntity.toUser(): User {
    return when (progress) {
        "1" -> User(
            id = id,
            progress = "himikase",
            learningSetsCount = learningSetsCount
        )
        "2" -> User(
            id = id,
            progress = "fuwoya",
            learningSetsCount = learningSetsCount
        )
        "3" -> User(
            id = id,
            progress = "ao",
            learningSetsCount = learningSetsCount
        )
        "4" -> User(
            id = id,
            progress = "tsuune",
            learningSetsCount = learningSetsCount
        )
        "5" -> User(
            id = id,
            progress = "kuherike",
            learningSetsCount = learningSetsCount
        )
        "6" -> User(
            id = id,
            progress = "konitana",
            learningSetsCount = learningSetsCount
        )
        "7" -> User(
            id = id,
            progress = "sumuroru",
            learningSetsCount = learningSetsCount
        )
        "8" -> User(
            id = id,
            progress = "shiimo",
            learningSetsCount = learningSetsCount
        )
        "9" -> User(
            id = id,
            progress = "toteso",
            learningSetsCount = learningSetsCount
        )
        "10" -> User(
            id = id,
            progress = "wanere",
            learningSetsCount = learningSetsCount
        )
        "11" -> User(
            id = id,
            progress = "noyumenu",
            learningSetsCount = learningSetsCount
        )
        "12" -> User(
            id = id,
            progress = "yohamaho",
            learningSetsCount = learningSetsCount
        )
        "13" -> User(
            id = id,
            progress = "sakichira",
            learningSetsCount = learningSetsCount
        )
        else -> User(
            id = id,
            progress = "-1",
            learningSetsCount = learningSetsCount
        )
    }
}