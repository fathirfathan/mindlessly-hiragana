package com.effatheresoft.mindlesslyhiragana.data

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert

@Dao
interface UserDao {
    @Upsert
    suspend fun upsertUser(user: UserRoomEntity)

    @Query("SELECT * FROM user WHERE id = 'localUser'")
    suspend fun getLocalUser(): UserRoomEntity

    @Query("SELECT * FROM user WHERE id = :id")
    suspend fun getUserById(id: String): UserRoomEntity?
}