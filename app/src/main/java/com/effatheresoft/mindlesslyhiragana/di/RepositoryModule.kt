package com.effatheresoft.mindlesslyhiragana.di

import com.effatheresoft.mindlesslyhiragana.data.local.UserDao
import com.effatheresoft.mindlesslyhiragana.data.repository.QuizVolatileDataSource
import com.effatheresoft.mindlesslyhiragana.data.repository.RefactoredQuizRepository
import com.effatheresoft.mindlesslyhiragana.data.repository.RefactoredUserRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

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