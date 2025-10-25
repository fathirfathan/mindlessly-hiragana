package com.effatheresoft.mindlesslyhiragana.data.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface UserDao {
    @Transaction
    @Query("SELECT * FROM user WHERE id = :id")
    fun getUserById(id: String): Flow<UserWithInteractions?>

    @Insert
    suspend fun insert(user: UserEntity): Long

    @Update
    suspend fun update(user: UserEntity): Int

    @Delete
    suspend fun delete(user: UserEntity)

    @Insert
    suspend fun insertInteraction(interaction: UserInteractionEntity)

    @Query("DELETE FROM user_interaction WHERE user_id = :userId")
    suspend fun clearInteractions(userId: String)
}
