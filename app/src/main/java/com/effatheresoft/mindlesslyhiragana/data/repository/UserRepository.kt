package com.effatheresoft.mindlesslyhiragana.data.repository

import com.effatheresoft.mindlesslyhiragana.data.model.User
import kotlinx.coroutines.flow.Flow

interface UserRepository {

    fun observeLocalUser(): Flow<User>

    suspend fun updateLocalUserProgress(progress: String)

    suspend fun updateLocalUserLearningSetsCount(count: Int)
}