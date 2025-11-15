package com.effatheresoft.mindlesslyhiragana.di

import android.content.Context
import androidx.room.Room
import com.effatheresoft.mindlesslyhiragana.data.DefaultDatabase
import com.effatheresoft.mindlesslyhiragana.data.UserDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Singleton
    @Provides
    fun provideDefaultDatabase(@ApplicationContext context: Context): DefaultDatabase {
        return Room.databaseBuilder(
            context.applicationContext,
            DefaultDatabase::class.java,
            "default_database.db"
        )
            .createFromAsset("database/default_database.db")
            .build()
    }

    @Provides
    fun provideUserDao(database: DefaultDatabase): UserDao = database.userDao()
}