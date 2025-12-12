package com.effatheresoft.mindlesslyhiragana.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.effatheresoft.mindlesslyhiragana.Constants.DEFAULT_DATABASE_USER_TABLE_NAME
import com.effatheresoft.mindlesslyhiragana.data.model.HiraganaCategory.AO
import com.effatheresoft.mindlesslyhiragana.data.model.HiraganaCategory.HIMIKASE
import com.effatheresoft.mindlesslyhiragana.data.model.HiraganaCategory.FUWOYA
import com.effatheresoft.mindlesslyhiragana.data.model.HiraganaCategory.KONITANA
import com.effatheresoft.mindlesslyhiragana.data.model.HiraganaCategory.KUHERIKE
import com.effatheresoft.mindlesslyhiragana.data.model.HiraganaCategory.NOYUMENU
import com.effatheresoft.mindlesslyhiragana.data.model.HiraganaCategory.SAKICHIRA
import com.effatheresoft.mindlesslyhiragana.data.model.HiraganaCategory.SHIIMO
import com.effatheresoft.mindlesslyhiragana.data.model.HiraganaCategory.SUMURORU
import com.effatheresoft.mindlesslyhiragana.data.model.HiraganaCategory.TOTESO
import com.effatheresoft.mindlesslyhiragana.data.model.HiraganaCategory.TSUUNE
import com.effatheresoft.mindlesslyhiragana.data.model.HiraganaCategory.WANERE
import com.effatheresoft.mindlesslyhiragana.data.model.HiraganaCategory.YOHAMAHO
import com.effatheresoft.mindlesslyhiragana.data.model.User

@Entity(tableName = DEFAULT_DATABASE_USER_TABLE_NAME)
data class UserRoomEntity(
    @PrimaryKey val id: String,
    val progress: String,
    val learningSetsCount: Int
)

fun UserRoomEntity.toUser(): User {
    return when (progress) {
        "1" -> User(
            id = id,
            progress = HIMIKASE.id,
            learningSetsCount = learningSetsCount
        )
        "2" -> User(
            id = id,
            progress = FUWOYA.id,
            learningSetsCount = learningSetsCount
        )
        "3" -> User(
            id = id,
            progress = AO.id,
            learningSetsCount = learningSetsCount
        )
        "4" -> User(
            id = id,
            progress = TSUUNE.id,
            learningSetsCount = learningSetsCount
        )
        "5" -> User(
            id = id,
            progress = KUHERIKE.id,
            learningSetsCount = learningSetsCount
        )
        "6" -> User(
            id = id,
            progress = KONITANA.id,
            learningSetsCount = learningSetsCount
        )
        "7" -> User(
            id = id,
            progress = SUMURORU.id,
            learningSetsCount = learningSetsCount
        )
        "8" -> User(
            id = id,
            progress = SHIIMO.id,
            learningSetsCount = learningSetsCount
        )
        "9" -> User(
            id = id,
            progress = TOTESO.id,
            learningSetsCount = learningSetsCount
        )
        "10" -> User(
            id = id,
            progress = WANERE.id,
            learningSetsCount = learningSetsCount
        )
        "11" -> User(
            id = id,
            progress = NOYUMENU.id,
            learningSetsCount = learningSetsCount
        )
        "12" -> User(
            id = id,
            progress = YOHAMAHO.id,
            learningSetsCount = learningSetsCount
        )
        "13" -> User(
            id = id,
            progress = SAKICHIRA.id,
            learningSetsCount = learningSetsCount
        )
        else -> User(
            id = id,
            progress = "-1",
            learningSetsCount = learningSetsCount
        )
    }
}