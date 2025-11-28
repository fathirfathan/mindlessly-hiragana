package com.effatheresoft.mindlesslyhiragana.data

import jakarta.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class DefaultUserRepository @Inject constructor(
    private val localDataSource: UserDao
): UserRepository {

    override fun observeLocalUser(): Flow<User> = localDataSource.observeLocalUser().map { it.toUser() }

    override suspend fun setLocalUserProgress(string: String) =
        localDataSource.upsertUser(
            User(
                id = "localUser",
                progress = string
            ).toUserRoomEntity()
        )
}