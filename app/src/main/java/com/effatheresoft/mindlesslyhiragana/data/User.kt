package com.effatheresoft.mindlesslyhiragana.data

data class User(
    val id: String,
    val progress: String
)

fun User.toUserRoomEntity(): UserRoomEntity {
    return when(progress) {
        "himikase"  -> this.copy(progress = "1").toUserRoomEntity()
        "fuwoya"    -> this.copy(progress = "2").toUserRoomEntity()
        "ao"        -> this.copy(progress = "3").toUserRoomEntity()
        "tsuune"    -> this.copy(progress = "4").toUserRoomEntity()
        "kuherike"  -> this.copy(progress = "5").toUserRoomEntity()
        "konitana"  -> this.copy(progress = "6").toUserRoomEntity()
        "sumuroru"  -> this.copy(progress = "7").toUserRoomEntity()
        "shiimo"    -> this.copy(progress = "8").toUserRoomEntity()
        "toteso"    -> this.copy(progress = "9").toUserRoomEntity()
        "wanere"    -> this.copy(progress = "10").toUserRoomEntity()
        "noyumenu"  -> this.copy(progress = "11").toUserRoomEntity()
        "yohamaho"  -> this.copy(progress = "12").toUserRoomEntity()
        "sakichira" -> this.copy(progress = "13").toUserRoomEntity()
        else -> this.copy(progress = "-1").toUserRoomEntity()
    }
}