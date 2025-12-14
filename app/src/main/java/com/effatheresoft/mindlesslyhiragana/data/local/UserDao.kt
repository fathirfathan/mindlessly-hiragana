package com.effatheresoft.mindlesslyhiragana.data.local

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import com.effatheresoft.mindlesslyhiragana.Constants
import kotlinx.coroutines.flow.Flow

@Dao
interface UserDao {
    @Upsert
    suspend fun upsertUser(user: UserRoomEntity)

    @Query("UPDATE user SET progress = :progress WHERE id = '${Constants.LOCAL_USER_ID}'")
    suspend fun updateLocalUserProgress(progress: String)

    @Query("UPDATE user SET learningSetsCount = :count WHERE id = '${Constants.LOCAL_USER_ID}'")
    suspend fun updateLocalUserLearningSetsCount(count: Int)

    @Query("UPDATE user SET isTestUnlocked = :isUnlocked WHERE id = '${Constants.LOCAL_USER_ID}'")
    suspend fun updateLocalUserIsTestUnlocked(isUnlocked: Boolean)

    @Query("SELECT * FROM user WHERE id = '${Constants.LOCAL_USER_ID}'")
    fun observeLocalUser(): Flow<UserRoomEntity>

    @Query("SELECT * FROM user WHERE id = :id")
    suspend fun getUserById(id: String): UserRoomEntity?
}