package com.effatheresoft.mindlesslyhiragana.data.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.effatheresoft.mindlesslyhiragana.data.UserInteraction
import kotlinx.coroutines.flow.Flow

@Dao
interface UserDao {
    @Query("SELECT * FROM user WHERE id = :id")
    fun getUserById(id: String): Flow<UserEntity?>

    @Insert
    suspend fun insert(user: UserEntity): Long

    @Update
    suspend fun update(user: UserEntity): Int

    @Delete
    suspend fun delete(user: UserEntity)

    @Query("UPDATE user SET user_interactions = :interactions WHERE id = :id")
    suspend fun updateUserInteractions(id: String, interactions: List<UserInteraction>)
}
