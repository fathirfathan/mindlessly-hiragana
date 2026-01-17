package com.effatheresoft.mindlesslyhiragana.di

import com.effatheresoft.mindlesslyhiragana.data.repository.QuizVolatileDataSource
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object VolatileDataSourceModule {
    @Singleton
    @Provides
    fun provideQuizVolatileDataSource() = QuizVolatileDataSource()
}