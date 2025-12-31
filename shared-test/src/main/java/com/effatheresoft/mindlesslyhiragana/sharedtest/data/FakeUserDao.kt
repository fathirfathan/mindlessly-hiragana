package com.effatheresoft.mindlesslyhiragana.sharedtest.data

import com.effatheresoft.mindlesslyhiragana.Constants.DEFAULT_LEARNING_SETS_COUNT
import com.effatheresoft.mindlesslyhiragana.Constants.LOCAL_USER_ID
import com.effatheresoft.mindlesslyhiragana.data.local.UserDao
import com.effatheresoft.mindlesslyhiragana.data.local.UserRoomEntity
import com.effatheresoft.mindlesslyhiragana.data.model.HiraganaCategory.HIMIKASE
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map

class FakeUserDao: UserDao {
    private val _users = MutableStateFlow(
        listOf(
            UserRoomEntity(
                LOCAL_USER_ID,
                HIMIKASE.id,
                DEFAULT_LEARNING_SETS_COUNT,
                false
            )
        )
    )

    override suspend fun upsertUser(user: UserRoomEntity) {
        val users = _users.value.toMutableList()
        users.replaceAll {
            if (it.id == user.id) user else it
        }
        _users.value = users
    }

    override suspend fun updateLocalUserProgress(progress: String) {
        val localUser = _users.value.first { it.id == LOCAL_USER_ID }
        upsertUser(localUser.copy(progress = progress))
    }

    override suspend fun updateLocalUserLearningSetsCount(count: Int) {
        val localUser = _users.value.first { it.id == LOCAL_USER_ID }
        upsertUser(localUser.copy(learningSetsCount = count))
    }

    override suspend fun updateLocalUserIsTestUnlocked(isUnlocked: Boolean) {
        val localUser = _users.value.first { it.id == LOCAL_USER_ID }
        upsertUser(localUser.copy(isTestUnlocked = isUnlocked))
    }

    override fun observeLocalUser(): Flow<UserRoomEntity> {
        return _users.map { users -> users.first { it.id == LOCAL_USER_ID } }
    }

    override suspend fun getUserById(id: String): UserRoomEntity? {
        return _users.value.firstOrNull { it.id == id }
    }
}