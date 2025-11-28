package com.effatheresoft.mindlesslyhiragana.data

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import kotlinx.coroutines.flow.Flow

@Dao
interface UserDao {
    @Upsert
    suspend fun upsertUser(user: UserRoomEntity)

    @Query("SELECT * FROM user WHERE id = 'localUser'")
    fun observeLocalUser(): Flow<UserRoomEntity>

    @Query("SELECT * FROM user WHERE id = :id")
    suspend fun getUserById(id: String): UserRoomEntity?
}