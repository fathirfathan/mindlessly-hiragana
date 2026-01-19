package com.effatheresoft.mindlesslyhiragana.data.local

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [LocalUserRoomEntity::class], version = 1)
abstract class DefaultDatabase: RoomDatabase() {
    abstract fun localUserDao(): LocalUserDao
}