package com.kaishijak.mindlesslyhiragana.sharedtest.di

import com.kaishijak.mindlesslyhiragana.data.local.LocalUserDao
import com.kaishijak.mindlesslyhiragana.data.repository.QuizVolatileDataSource
import com.kaishijak.mindlesslyhiragana.di.DatabaseModule
import com.kaishijak.mindlesslyhiragana.di.VolatileDataSourceModule
import com.kaishijak.mindlesslyhiragana.sharedtest.data.FakeQuizVolatileDataSource
import com.kaishijak.mindlesslyhiragana.sharedtest.data.FakeUserDao
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
    fun provideFakeUserDao(): LocalUserDao = FakeUserDao()
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