package com.effatheresoft.mindlesslyhiragana.data

data class User(
    val id: String,
    val progress: String,
    val learningSetsCount: Int
)

fun User.toUserRoomEntity(): UserRoomEntity {
    return when(progress) {
        "himikase" -> UserRoomEntity(
            id = id,
            progress = "1",
            learningSetsCount = learningSetsCount
        )
        "fuwoya" -> UserRoomEntity(
            id = id,
            progress = "2",
            learningSetsCount = learningSetsCount
        )
        "ao" -> UserRoomEntity(
            id = id,
            progress = "3",
            learningSetsCount = learningSetsCount
        )
        "tsuune" -> UserRoomEntity(
            id = id,
            progress = "4",
            learningSetsCount = learningSetsCount
        )
        "kuherike" -> UserRoomEntity(
            id = id,
            progress = "5",
            learningSetsCount = learningSetsCount
        )
        "konitana" -> UserRoomEntity(
            id = id,
            progress = "6",
            learningSetsCount = learningSetsCount
        )
        "sumuroru" -> UserRoomEntity(
            id = id,
            progress = "7",
            learningSetsCount = learningSetsCount
        )
        "shiimo" -> UserRoomEntity(
            id = id,
            progress = "8",
            learningSetsCount = learningSetsCount
        )
        "toteso" -> UserRoomEntity(
            id = id,
            progress = "9",
            learningSetsCount = learningSetsCount
        )
        "wanere" -> UserRoomEntity(
            id = id,
            progress = "10",
            learningSetsCount = learningSetsCount
        )
        "noyumenu" -> UserRoomEntity(
            id = id,
            progress = "11",
            learningSetsCount = learningSetsCount
        )
        "yohamaho" -> UserRoomEntity(
            id = id,
            progress = "12",
            learningSetsCount = learningSetsCount
        )
        "sakichira" -> UserRoomEntity(
            id = id,
            progress = "13",
            learningSetsCount = learningSetsCount
        )
        else -> UserRoomEntity(
            id = id,
            progress = "-1",
            learningSetsCount = learningSetsCount
        )
    }
}