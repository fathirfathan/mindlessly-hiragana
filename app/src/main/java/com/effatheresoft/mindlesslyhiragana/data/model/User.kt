package com.effatheresoft.mindlesslyhiragana.data.model

import com.effatheresoft.mindlesslyhiragana.data.model.HiraganaCategory.AO
import com.effatheresoft.mindlesslyhiragana.data.model.HiraganaCategory.HIMIKASE
import com.effatheresoft.mindlesslyhiragana.data.model.HiraganaCategory.FUWOYA
import com.effatheresoft.mindlesslyhiragana.data.model.HiraganaCategory.TSUUNE
import com.effatheresoft.mindlesslyhiragana.data.model.HiraganaCategory.KUHERIKE
import com.effatheresoft.mindlesslyhiragana.data.model.HiraganaCategory.KONITANA
import com.effatheresoft.mindlesslyhiragana.data.model.HiraganaCategory.SUMURORU
import com.effatheresoft.mindlesslyhiragana.data.model.HiraganaCategory.SHIIMO
import com.effatheresoft.mindlesslyhiragana.data.model.HiraganaCategory.TOTESO
import com.effatheresoft.mindlesslyhiragana.data.model.HiraganaCategory.WANERE
import com.effatheresoft.mindlesslyhiragana.data.model.HiraganaCategory.NOYUMENU
import com.effatheresoft.mindlesslyhiragana.data.model.HiraganaCategory.YOHAMAHO
import com.effatheresoft.mindlesslyhiragana.data.model.HiraganaCategory.SAKICHIRA
import com.effatheresoft.mindlesslyhiragana.data.local.UserRoomEntity

data class User(
    val id: String,
    val progress: String,
    val learningSetsCount: Int
)

fun User.toUserRoomEntity(): UserRoomEntity {
    return when(progress) {
        HIMIKASE.id -> UserRoomEntity(
            id = id,
            progress = "1",
            learningSetsCount = learningSetsCount
        )
        FUWOYA.id -> UserRoomEntity(
            id = id,
            progress = "2",
            learningSetsCount = learningSetsCount
        )
        AO.id -> UserRoomEntity(
            id = id,
            progress = "3",
            learningSetsCount = learningSetsCount
        )
        TSUUNE.id -> UserRoomEntity(
            id = id,
            progress = "4",
            learningSetsCount = learningSetsCount
        )
        KUHERIKE.id -> UserRoomEntity(
            id = id,
            progress = "5",
            learningSetsCount = learningSetsCount
        )
        KONITANA.id -> UserRoomEntity(
            id = id,
            progress = "6",
            learningSetsCount = learningSetsCount
        )
        SUMURORU.id -> UserRoomEntity(
            id = id,
            progress = "7",
            learningSetsCount = learningSetsCount
        )
        SHIIMO.id -> UserRoomEntity(
            id = id,
            progress = "8",
            learningSetsCount = learningSetsCount
        )
        TOTESO.id -> UserRoomEntity(
            id = id,
            progress = "9",
            learningSetsCount = learningSetsCount
        )
        WANERE.id -> UserRoomEntity(
            id = id,
            progress = "10",
            learningSetsCount = learningSetsCount
        )
        NOYUMENU.id -> UserRoomEntity(
            id = id,
            progress = "11",
            learningSetsCount = learningSetsCount
        )
        YOHAMAHO.id -> UserRoomEntity(
            id = id,
            progress = "12",
            learningSetsCount = learningSetsCount
        )
        SAKICHIRA.id -> UserRoomEntity(
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