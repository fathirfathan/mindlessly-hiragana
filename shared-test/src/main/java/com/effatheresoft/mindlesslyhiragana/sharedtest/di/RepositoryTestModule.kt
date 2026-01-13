package com.effatheresoft.mindlesslyhiragana.di

import com.effatheresoft.mindlesslyhiragana.data.FakeUserRepository
import com.effatheresoft.mindlesslyhiragana.data.local.UserDao
import com.effatheresoft.mindlesslyhiragana.data.local.UserRoomEntity
import com.effatheresoft.mindlesslyhiragana.data.repository.UserRepository
import com.effatheresoft.mindlesslyhiragana.data.repository.QuizRepository
import com.effatheresoft.mindlesslyhiragana.sharedtest.data.FakeQuizRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.components.SingletonComponent
import dagger.hilt.testing.TestInstallIn
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import javax.inject.Singleton

class FakeUserDao: UserDao {
    private val localUser = MutableStateFlow(
        UserRoomEntity(
            id = "localUser",
            progress = "1",
            learningSetsCount = 5,
            isTestUnlocked = false
        )
    )

    override suspend fun upsertUser(user: UserRoomEntity) {}

    override suspend fun updateLocalUserProgress(progress: String) {}

    override suspend fun updateLocalUserLearningSetsCount(count: Int) {}

    override suspend fun updateLocalUserIsTestUnlocked(isUnlocked: Boolean) {}

    override fun observeLocalUser(): Flow<UserRoomEntity> = localUser
    override suspend fun getUserById(id: String): UserRoomEntity? = when(id) {
        "localUser" -> localUser.value
        else -> null
    }
}

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