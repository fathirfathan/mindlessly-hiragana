package com.effatheresoft.mindlesslyhiragana.data

import jakarta.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

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
        "himikase"  -> "1"
        "fuwoya"    -> "2"
        "ao"        -> "3"
        "tsuune"    -> "4"
        "kuherike"  -> "5"
        "konitana"  -> "6"
        "sumuroru"  -> "7"
        "shiimo"    -> "8"
        "toteso"    -> "9"
        "wanere"    -> "10"
        "noyumenu"  -> "11"
        "yohamaho"  -> "12"
        "sakichira" -> "13"
        else -> "-1"
    }
}