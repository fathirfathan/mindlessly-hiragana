package com.effatheresoft.mindlesslyhiragana

import android.app.Application
import androidx.room.Room

import com.effatheresoft.mindlesslyhiragana.data.HiraganaDataSource
import com.effatheresoft.mindlesslyhiragana.data.HiraganaRepository
import com.effatheresoft.mindlesslyhiragana.data.UserLocalDataSource
import com.effatheresoft.mindlesslyhiragana.data.UserRepository
import com.effatheresoft.mindlesslyhiragana.data.local.DefaultDatabase

class DefaultApplication : Application() {
    val hiraganaDataSource by lazy { HiraganaDataSource() }
    val hiraganaRepository by lazy { HiraganaRepository(hiraganaDataSource) }

    val defaultDatabase by lazy {
        Room.databaseBuilder(
            this, // 'this' refers to the DefaultApplication instance, which is the context.
            DefaultDatabase::class.java,
            "default-database"
        ).build()
    }

    // These can also be lazy to ensure they are created only when needed.
    val userLocalDataSource by lazy { UserLocalDataSource(defaultDatabase.userDao()) }
    val userRepository by lazy { UserRepository(userLocalDataSource) }
}

