package com.effatheresoft.mindlesslyhiragana.di

import android.content.Context
import androidx.room.Room
import com.effatheresoft.mindlesslyhiragana.Constants.DEFAULT_DATABASE_NAME
import com.effatheresoft.mindlesslyhiragana.Constants.PREPOPULATED_DATABASE_FILEPATH
import com.effatheresoft.mindlesslyhiragana.data.local.DefaultDatabase
import com.effatheresoft.mindlesslyhiragana.data.repository.DefaultUserRepository
import com.effatheresoft.mindlesslyhiragana.data.local.UserDao
import com.effatheresoft.mindlesslyhiragana.data.repository.UserRepository
import com.effatheresoft.mindlesslyhiragana.data.repository.DefaultQuizRepository
import com.effatheresoft.mindlesslyhiragana.data.repository.QuizRepository
import com.effatheresoft.mindlesslyhiragana.data.repository.RefactoredUserRepository
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
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
}

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Singleton
    @Provides
    fun provideDefaultDatabase(@ApplicationContext context: Context): DefaultDatabase {
        return Room.databaseBuilder(
            context.applicationContext,
            DefaultDatabase::class.java,
            DEFAULT_DATABASE_NAME
        )
            .createFromAsset(PREPOPULATED_DATABASE_FILEPATH)
            .build()
    }

    @Provides
    fun provideUserDao(database: DefaultDatabase): UserDao = database.userDao()
}