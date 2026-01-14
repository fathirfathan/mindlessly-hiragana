package com.effatheresoft.mindlesslyhiragana.sharedtest.data

import com.effatheresoft.mindlesslyhiragana.data.local.UserDao
import com.effatheresoft.mindlesslyhiragana.data.local.UserRoomEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map

class FakeUserDao: UserDao {
    private val localUserEntity = UserRoomEntity(
        id = "localUser",
        progress = "1",
        learningSetsCount = 5,
        isTestUnlocked = false
    )
    private val userEntities = MutableStateFlow(hashMapOf(localUserEntity.id to localUserEntity))

    private fun deepCloneUserMap(map: HashMap<String, UserRoomEntity>): HashMap<String, UserRoomEntity> {
        val clonedMap = hashMapOf<String, UserRoomEntity>()
        for ((key, value) in map) {
            clonedMap[key] = value.copy()
        }
        return clonedMap
    }

    override suspend fun upsertUser(user: UserRoomEntity) {}

    override suspend fun updateLocalUserProgress(progress: String) {}

    override suspend fun updateLocalUserLearningSetsCount(count: Int) {
        val updatedMap = deepCloneUserMap(userEntities.value)
        updatedMap["localUser"] = updatedMap["localUser"]!!.copy(learningSetsCount = count)
        userEntities.value = updatedMap
    }

    override suspend fun updateLocalUserIsTestUnlocked(isUnlocked: Boolean) {}

    override fun observeLocalUser(): Flow<UserRoomEntity> = userEntities.map { it.getValue("localUser") }

    override suspend fun getUserById(id: String): UserRoomEntity? = userEntities.value[id]
}