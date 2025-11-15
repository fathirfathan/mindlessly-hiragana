package com.effatheresoft.mindlesslyhiragana.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "user")
data class UserRoomEntity(
    @PrimaryKey val id: String,
    val progress: String,
)