package com.effatheresoft.mindlesslyhiragana.data

data class User(
    val id: String,
    val progress: String
)

fun User.toUserRoomEntity(): UserRoomEntity {
    return when(progress) {
        "himikase" -> UserRoomEntity(
            id = id,
            progress = "1"
        )
        "fuwoya" -> UserRoomEntity(
            id = id,
            progress = "2"
        )
        "ao" -> UserRoomEntity(
            id = id,
            progress = "3"
        )
        "tsuune" -> UserRoomEntity(
            id = id,
            progress = "4"
        )
        "kuherike" -> UserRoomEntity(
            id = id,
            progress = "5"
        )
        "konitana" -> UserRoomEntity(
            id = id,
            progress = "6"
        )
        "sumuroru" -> UserRoomEntity(
            id = id,
            progress = "7"
        )
        "shiimo" -> UserRoomEntity(
            id = id,
            progress = "8"
        )
        "toteso" -> UserRoomEntity(
            id = id,
            progress = "9"
        )
        "wanere" -> UserRoomEntity(
            id = id,
            progress = "10"
        )
        "noyumenu" -> UserRoomEntity(
            id = id,
            progress = "11"
        )
        "yohamaho" -> UserRoomEntity(
            id = id,
            progress = "12"
        )
        "sakichira" -> UserRoomEntity(
            id = id,
            progress = "13"
        )
        else -> UserRoomEntity(
            id = id,
            progress = "-1"
        )
    }
}