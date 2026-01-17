package com.effatheresoft.mindlesslyhiragana.di

import android.content.Context
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.effatheresoft.mindlesslyhiragana.Constants.DEFAULT_DATABASE_NAME
import com.effatheresoft.mindlesslyhiragana.data.local.DefaultDatabase
import com.effatheresoft.mindlesslyhiragana.data.local.UserDao
import com.effatheresoft.mindlesslyhiragana.data.local.UserRoomEntity
import com.effatheresoft.mindlesslyhiragana.data.repository.DefaultQuizRepository
import com.effatheresoft.mindlesslyhiragana.data.repository.DefaultUserRepository
import com.effatheresoft.mindlesslyhiragana.data.repository.QuizRepository
import com.effatheresoft.mindlesslyhiragana.data.repository.QuizVolatileDataSource
import com.effatheresoft.mindlesslyhiragana.data.repository.RefactoredQuizRepository
import com.effatheresoft.mindlesslyhiragana.data.repository.RefactoredUserRepository
import com.effatheresoft.mindlesslyhiragana.data.repository.UserRepository
import dagger.Binds
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
abstract class RepositoryModule {

    @Singleton
    @Binds
    abstract fun bindUserRepository(repository: DefaultUserRepository): UserRepository

    @Singleton
    @Binds
    abstract fun bindQuizRepository(repository: DefaultQuizRepository): QuizRepository
}

@Module
@InstallIn(SingletonComponent::class)
object RepositoryProviderModule {
    @Singleton
    @Provides
    fun provideRefactoredUserRepository(userDao: UserDao) =
        RefactoredUserRepository(userDao)

    @Singleton
    @Provides
    fun provideRefactoredQuizRepository(
        userRepository: RefactoredUserRepository,
        quizVolatileDataSource: QuizVolatileDataSource
    ) = RefactoredQuizRepository(userRepository, quizVolatileDataSource)
}

@Module
@InstallIn(SingletonComponent::class)
object VolatileDataSourceModule {
    @Singleton
    @Provides
    fun provideQuizVolatileDataSource() = QuizVolatileDataSource()
}

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Singleton
    @Provides
    fun provideDefaultDatabase(
        @ApplicationContext context: Context,
        userDaoProvider: Provider<UserDao>
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
                        prePopulateDatabase(userDaoProvider.get())
                    }
                }
            })
            .build()
    }

    private suspend fun prePopulateDatabase(userDao: UserDao) {
        userDao.upsertUser(
            UserRoomEntity(
                id = "localUser",
                progress = "1",
                learningSetsCount = 5,
                isTestUnlocked = false
            )
        )
    }

    @Provides
    fun provideUserDao(database: DefaultDatabase): UserDao = database.userDao()
}