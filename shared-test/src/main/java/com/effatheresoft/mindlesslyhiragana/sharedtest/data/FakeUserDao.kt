package com.effatheresoft.mindlesslyhiragana.sharedtest.data

import com.effatheresoft.mindlesslyhiragana.data.local.LocalUserDao
import com.effatheresoft.mindlesslyhiragana.data.local.LocalUserRoomEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow

class FakeUserDao: LocalUserDao {
    private val localUserRoomEntity = MutableStateFlow(LocalUserRoomEntity.default)

    override suspend fun upsertLocalUser(user: LocalUserRoomEntity) {
        localUserRoomEntity.value = user
    }

    override suspend fun updateHighestCategory(category: String) {
        localUserRoomEntity.value = localUserRoomEntity.value.copy(highestCategory = category)
    }

    override suspend fun updateRepeatCategoryCount(count: Int) {
        localUserRoomEntity.value = localUserRoomEntity.value.copy(repeatCategoryCount = count)
    }

    override suspend fun updateIsTestUnlocked(isUnlocked: Boolean) {
        localUserRoomEntity.value = localUserRoomEntity.value.copy(isTestUnlocked = isUnlocked)
    }

    override fun observeLocalUser(): Flow<LocalUserRoomEntity> = localUserRoomEntity
}