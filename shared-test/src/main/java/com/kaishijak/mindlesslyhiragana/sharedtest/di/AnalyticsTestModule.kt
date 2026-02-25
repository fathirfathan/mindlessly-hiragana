package com.kaishijak.mindlesslyhiragana.sharedtest.di

import com.kaishijak.mindlesslyhiragana.analytics.Analytics
import com.kaishijak.mindlesslyhiragana.di.AnalyticsModule
import com.kaishijak.mindlesslyhiragana.sharedtest.analytics.FakeAnalytics
import dagger.Module
import dagger.Provides
import dagger.hilt.components.SingletonComponent
import dagger.hilt.testing.TestInstallIn

@Module
@TestInstallIn(
    components = [SingletonComponent::class],
    replaces = [AnalyticsModule::class]
)
object AnalyticsTestModule {
    @Provides
    fun provideFakeFirebaseAnalytics(): Analytics = FakeAnalytics()
}