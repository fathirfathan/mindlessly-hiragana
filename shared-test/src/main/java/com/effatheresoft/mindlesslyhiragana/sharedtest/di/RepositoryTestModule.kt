package com.effatheresoft.mindlesslyhiragana.di

import com.effatheresoft.mindlesslyhiragana.data.local.UserDao
import com.effatheresoft.mindlesslyhiragana.data.repository.QuizVolatileDataSource
import com.effatheresoft.mindlesslyhiragana.sharedtest.data.FakeQuizVolatileDataSource
import com.effatheresoft.mindlesslyhiragana.sharedtest.data.FakeUserDao
import dagger.Module
import dagger.Provides
import dagger.hilt.components.SingletonComponent
import dagger.hilt.testing.TestInstallIn
import javax.inject.Singleton

@Module
@TestInstallIn(
    components = [SingletonComponent::class],
    replaces = [DatabaseModule::class]
)
object DatabaseTestModule {
    @Singleton
    @Provides
    fun provideFakeUserDao(): UserDao = FakeUserDao()
}

@Module
@TestInstallIn(
    components = [SingletonComponent::class],
    replaces = [VolatileDataSourceModule::class]
)
object VolatileDataSourceTestModule {
    @Singleton
    @Provides
    fun provideFakeQuizVolatileDataSource(): QuizVolatileDataSource = FakeQuizVolatileDataSource()
}

//@Module
//@TestInstallIn(
//    components = [SingletonComponent::class],
//    replaces = [RepositoryModule::class]
//)
//object RepositoryTestModule {
//
//    @Singleton
//    @Provides
//    fun provideUserRepository(): UserRepository {
//        return FakeUserRepository()
//    }
//
//    @Singleton
//    @Provides
//    fun provideQuizRepository(): QuizRepository {
//        return FakeQuizRepository()
//    }
//}