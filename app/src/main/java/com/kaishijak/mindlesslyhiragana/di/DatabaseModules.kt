package com.kaishijak.mindlesslyhiragana.di

import android.content.Context
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.kaishijak.mindlesslyhiragana.Constants.DEFAULT_DATABASE_NAME
import com.kaishijak.mindlesslyhiragana.data.local.DefaultDatabase
import com.kaishijak.mindlesslyhiragana.data.local.LocalUserDao
import com.kaishijak.mindlesslyhiragana.data.local.LocalUserRoomEntity
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Provider
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Singleton
    @Provides
    fun provideDefaultDatabase(
        @ApplicationContext context: Context,
        localUserDaoProvider: Provider<LocalUserDao>
    ): DefaultDatabase {
        return Room.databaseBuilder(
            context.applicationContext,
            DefaultDatabase::class.java,
            DEFAULT_DATABASE_NAME
        )
            .addCallback(object : RoomDatabase.Callback() {
                override fun onCreate(db: SupportSQLiteDatabase) {
                    super.onCreate(db)

                    CoroutineScope(Dispatchers.IO).launch {
                        prePopulateDatabase(localUserDaoProvider.get())
                    }
                }
            })
            .build()
    }

    private suspend fun prePopulateDatabase(localUserDao: LocalUserDao) {
        localUserDao.upsertLocalUser(LocalUserRoomEntity.default)
    }

    @Provides
    fun provideLocalUserDao(database: DefaultDatabase): LocalUserDao = database.localUserDao()
}