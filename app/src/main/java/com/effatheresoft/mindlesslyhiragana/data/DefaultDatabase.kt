package com.effatheresoft.mindlesslyhiragana.data

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [UserRoomEntity::class], version = 1)
abstract class DefaultDatabase: RoomDatabase() {
    abstract fun userDao(): UserDao
}