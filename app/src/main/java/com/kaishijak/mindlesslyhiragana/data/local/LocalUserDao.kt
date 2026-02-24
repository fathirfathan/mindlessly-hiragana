package com.kaishijak.mindlesslyhiragana.data.local

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import com.kaishijak.mindlesslyhiragana.Constants
import kotlinx.coroutines.flow.Flow

@Dao
interface LocalUserDao {
    @Upsert
    suspend fun upsertLocalUser(user: LocalUserRoomEntity)

    @Query("UPDATE user SET highestCategory = :category WHERE id = '${Constants.LOCAL_USER_ID}'")
    suspend fun updateHighestCategory(category: String)

    @Query("UPDATE user SET repeatCategoryCount = :count WHERE id = '${Constants.LOCAL_USER_ID}'")
    suspend fun updateRepeatCategoryCount(count: Int)

    @Query("UPDATE user SET isTestUnlocked = :isUnlocked WHERE id = '${Constants.LOCAL_USER_ID}'")
    suspend fun updateIsTestUnlocked(isUnlocked: Boolean)

    @Query("SELECT * FROM user WHERE id = '${Constants.LOCAL_USER_ID}'")
    fun observeLocalUser(): Flow<LocalUserRoomEntity>
}