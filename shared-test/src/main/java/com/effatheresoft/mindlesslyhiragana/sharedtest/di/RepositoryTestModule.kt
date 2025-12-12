package com.effatheresoft.mindlesslyhiragana.di

import com.effatheresoft.mindlesslyhiragana.data.FakeUserRepository
import com.effatheresoft.mindlesslyhiragana.data.repository.UserRepository
import com.effatheresoft.mindlesslyhiragana.data.repository.QuizRepository
import com.effatheresoft.mindlesslyhiragana.sharedtest.data.FakeQuizRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.components.SingletonComponent
import dagger.hilt.testing.TestInstallIn
import javax.inject.Singleton

@Module
@TestInstallIn(
    components = [SingletonComponent::class],
    replaces = [RepositoryModule::class]
)
object RepositoryTestModule {

    @Singleton
    @Provides
    fun provideUserRepository(): UserRepository {
        return FakeUserRepository()
    }

    @Singleton
    @Provides
    fun provideQuizRepository(): QuizRepository {
        return FakeQuizRepository()
    }
}