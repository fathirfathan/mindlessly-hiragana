package com.kaishijak.mindlesslyhiragana.di

import com.kaishijak.mindlesslyhiragana.analytics.Analytics
import com.kaishijak.mindlesslyhiragana.analytics.DefaultAnalytics
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object AnalyticsModule {
    @Provides
    fun provideFirebaseAnalytics(): Analytics = DefaultAnalytics()
}