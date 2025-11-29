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
        "1" -> this.copy(progress = "himikase").toUser()
        "2" -> this.copy(progress = "fuwoya").toUser()
        "3" -> this.copy(progress = "ao").toUser()
        "4" -> this.copy(progress = "tsuune").toUser()
        "5" -> this.copy(progress = "kuherike").toUser()
        "6" -> this.copy(progress = "konitana").toUser()
        "7" -> this.copy(progress = "sumuroru").toUser()
        "8" -> this.copy(progress = "shiimo").toUser()
        "9" -> this.copy(progress = "toteso").toUser()
        "10" -> this.copy(progress = "wanere").toUser()
        "11" -> this.copy(progress = "noyumenu").toUser()
        "12" -> this.copy(progress = "yohamaho").toUser()
        "13" -> this.copy(progress = "sakichira").toUser()
        else -> this.copy(progress = "-1").toUser()
    }
}