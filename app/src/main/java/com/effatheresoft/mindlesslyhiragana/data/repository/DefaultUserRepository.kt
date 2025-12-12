package com.effatheresoft.mindlesslyhiragana.data.repository

import jakarta.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import com.effatheresoft.mindlesslyhiragana.data.model.HiraganaCategory.HIMIKASE
import com.effatheresoft.mindlesslyhiragana.data.model.HiraganaCategory.FUWOYA
import com.effatheresoft.mindlesslyhiragana.data.model.HiraganaCategory.AO
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
import com.effatheresoft.mindlesslyhiragana.data.model.User
import com.effatheresoft.mindlesslyhiragana.data.local.UserDao
import com.effatheresoft.mindlesslyhiragana.data.local.toUser

class DefaultUserRepository @Inject constructor(
    private val localDataSource: UserDao
): UserRepository {

    override fun observeLocalUser(): Flow<User> = localDataSource.observeLocalUser().map { it.toUser() }

    override suspend fun updateLocalUserProgress(progress: String) =
        localDataSource.updateLocalUserProgress(progress.toRoomEntityProgress())
    override suspend fun updateLocalUserLearningSetsCount(count: Int) =
        localDataSource.updateLocalUserLearningSetsCount(count)
}

private fun String.toRoomEntityProgress(): String {
    return when(this) {
        HIMIKASE.id  -> "1"
        FUWOYA.id    -> "2"
        AO.id        -> "3"
        TSUUNE.id    -> "4"
        KUHERIKE.id  -> "5"
        KONITANA.id  -> "6"
        SUMURORU.id  -> "7"
        SHIIMO.id    -> "8"
        TOTESO.id    -> "9"
        WANERE.id    -> "10"
        NOYUMENU.id  -> "11"
        YOHAMAHO.id  -> "12"
        SAKICHIRA.id -> "13"
        else -> "-1"
    }
}