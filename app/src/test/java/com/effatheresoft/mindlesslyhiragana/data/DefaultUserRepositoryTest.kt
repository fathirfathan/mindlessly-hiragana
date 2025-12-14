package com.effatheresoft.mindlesslyhiragana.data

import com.effatheresoft.mindlesslyhiragana.Constants.DEFAULT_LEARNING_SETS_COUNT
import com.effatheresoft.mindlesslyhiragana.Constants.LOCAL_USER_ID
import com.effatheresoft.mindlesslyhiragana.MainCoroutineRule
import com.effatheresoft.mindlesslyhiragana.data.model.HiraganaCategory.HIMIKASE
import com.effatheresoft.mindlesslyhiragana.data.model.HiraganaCategory.FUWOYA
import com.effatheresoft.mindlesslyhiragana.data.local.UserDao
import com.effatheresoft.mindlesslyhiragana.data.local.UserRoomEntity
import com.effatheresoft.mindlesslyhiragana.data.model.User
import com.effatheresoft.mindlesslyhiragana.data.repository.DefaultUserRepository
import com.effatheresoft.mindlesslyhiragana.data.repository.UserRepository
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class FakeUserDao: UserDao {
    var localUserEntity = UserRoomEntity(
        id = LOCAL_USER_ID,
        progress = "1",
        learningSetsCount = DEFAULT_LEARNING_SETS_COUNT,
        isTestUnlocked = false
    )
    val userEntities = hashMapOf(localUserEntity.id to localUserEntity)

    override suspend fun upsertUser(user: UserRoomEntity) {
        userEntities[user.id] = user
    }

    override suspend  fun updateLocalUserProgress(progress: String) {
        userEntities[LOCAL_USER_ID] = userEntities[LOCAL_USER_ID]!!.copy(progress = progress)
    }

    override suspend fun updateLocalUserLearningSetsCount(count: Int) {
        userEntities[LOCAL_USER_ID] = userEntities[LOCAL_USER_ID]!!.copy(learningSetsCount = count)
    }

    override suspend fun updateLocalUserIsTestUnlocked(isUnlocked: Boolean) {
        userEntities[LOCAL_USER_ID] = userEntities[LOCAL_USER_ID]!!.copy(isTestUnlocked = isUnlocked)
    }

    override fun observeLocalUser(): Flow<UserRoomEntity> = flow {
        emit(userEntities[LOCAL_USER_ID] ?: localUserEntity)
    }

    override suspend fun getUserById(id: String): UserRoomEntity? {
        return userEntities[id]
    }

}

class DefaultUserRepositoryTest {
    private lateinit var defaultUserRepository: UserRepository

    @OptIn(ExperimentalCoroutinesApi::class)
    @get:Rule
    val mainCoroutineRule = MainCoroutineRule()

    @Before
    fun setupRepository() = runTest {
        defaultUserRepository = DefaultUserRepository(FakeUserDao())

        val localUser = User(LOCAL_USER_ID, HIMIKASE.id, DEFAULT_LEARNING_SETS_COUNT, false)
        defaultUserRepository.updateLocalUserProgress(localUser.progress)
    }

    @Test
    fun whileObservingLocalUser_whenProgressIsUpdated_thenLocalUserIsUpdated() = runTest {
        // While observing local user
        val observedLocalUser = defaultUserRepository.observeLocalUser()

        // And local user is updated
        val newProgress = FUWOYA.id
        defaultUserRepository.updateLocalUserProgress(newProgress)

        // Then the observed user sends the updated value
        assertEquals(newProgress, observedLocalUser.first().progress)
    }

    @Test
    fun whileObservingLocalUser_whenLearningSetsCountIsUpdated_thenLocalUserIsUpdated() = runTest {
        // While observing local user
        val observedLocalUser = defaultUserRepository.observeLocalUser()

        // And local user is updated
        val newLearningSetsCount = when {
            DEFAULT_LEARNING_SETS_COUNT >= 10 -> 9
            else -> DEFAULT_LEARNING_SETS_COUNT + 1
        }
        defaultUserRepository.updateLocalUserLearningSetsCount(newLearningSetsCount)

        // Then the observed user sends the updated values
        assertEquals(newLearningSetsCount, observedLocalUser.first().learningSetsCount)
    }

    @Test
    fun whileObservingLocalUser_whenIsTestUnlockedIsUpdated_thenLocalUserIsUpdated() = runTest {
        // While observing local user
        val observedLocalUser = defaultUserRepository.observeLocalUser()

        // And local user is updated
        val newIsTestUnlocked = true
        defaultUserRepository.updateLocalUserIsTestUnlocked(newIsTestUnlocked)

        // Then the observed user sends the updated value
        assertEquals(newIsTestUnlocked, observedLocalUser.first().isTestUnlocked)
    }
}