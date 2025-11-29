package com.effatheresoft.mindlesslyhiragana.data

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import com.effatheresoft.mindlesslyhiragana.Constants.LOCAL_USER_ID
import kotlinx.coroutines.flow.Flow

@Dao
interface UserDao {
    @Upsert
    suspend fun upsertUser(user: UserRoomEntity)

    @Query("UPDATE user SET progress = :progress WHERE id = '$LOCAL_USER_ID'")
    suspend fun updateLocalUserProgress(progress: String)

    @Query("UPDATE user SET learningSetsCount = :count WHERE id = '$LOCAL_USER_ID'")
    suspend fun updateLocalUserLearningSetsCount(count: Int)

    @Query("SELECT * FROM user WHERE id = '$LOCAL_USER_ID'")
    fun observeLocalUser(): Flow<UserRoomEntity>

    @Query("SELECT * FROM user WHERE id = :id")
    suspend fun getUserById(id: String): UserRoomEntity?
}