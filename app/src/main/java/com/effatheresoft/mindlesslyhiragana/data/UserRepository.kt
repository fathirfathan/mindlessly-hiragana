package com.effatheresoft.mindlesslyhiragana.data

import kotlinx.coroutines.flow.Flow


interface UserRepository {

    fun observeLocalUser(): Flow<User>

    suspend fun updateLocalUserProgress(progress: String)

    suspend fun updateLocalUserLearningSetsCount(count: Int)
}