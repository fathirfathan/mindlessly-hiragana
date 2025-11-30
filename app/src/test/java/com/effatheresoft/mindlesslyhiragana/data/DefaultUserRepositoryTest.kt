package com.effatheresoft.mindlesslyhiragana.data

import com.effatheresoft.mindlesslyhiragana.Constants.LOCAL_USER_ID
import com.effatheresoft.mindlesslyhiragana.MainCoroutineRule
import com.effatheresoft.mindlesslyhiragana.data.HiraganaCategory.HIMIKASE
import com.effatheresoft.mindlesslyhiragana.data.HiraganaCategory.FUWOYA
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
        learningSetsCount = 5
    )
    val localUserEntities = hashMapOf(localUserEntity.id to localUserEntity)

    override suspend fun upsertUser(user: UserRoomEntity) {
        localUserEntities[user.id] = user
    }

    override suspend  fun updateLocalUserProgress(progress: String) {
        localUserEntities[LOCAL_USER_ID] = localUserEntities[LOCAL_USER_ID]!!.copy(progress = progress)
    }

    override suspend fun updateLocalUserLearningSetsCount(count: Int) {
        localUserEntities[LOCAL_USER_ID] = localUserEntities[LOCAL_USER_ID]!!.copy(learningSetsCount = count)
    }

    override fun observeLocalUser(): Flow<UserRoomEntity> = flow {
        emit(localUserEntities[LOCAL_USER_ID] ?: localUserEntity)
    }

    override suspend fun getUserById(id: String): UserRoomEntity? {
        return localUserEntities[id]
    }

}

class DefaultUserRepositoryTest {
    private lateinit var defaultUserRepository: UserRepository

    @OptIn(ExperimentalCoroutinesApi::class)
    @get:Rule
    val mainCoroutineRule = MainCoroutineRule()

    @Before
    fun setupRepository() {
        defaultUserRepository = DefaultUserRepository(FakeUserDao())
    }

    @Test
    fun whileObservingLocalUser_whenProgressIsUpdated_thenLocalUserProgressIsUpdated() = runTest {
        // While observing local user
        val localUser = User(LOCAL_USER_ID, HIMIKASE.id, 5)
        defaultUserRepository.updateLocalUserProgress(localUser.progress)
        val observedLocalUser = defaultUserRepository.observeLocalUser()

        // And local user is updated
        val newlocalUser = User(LOCAL_USER_ID, FUWOYA.id, 5)
        defaultUserRepository.updateLocalUserProgress(newlocalUser.progress)

        // Then the observed user sends the updated values
        val latestData = observedLocalUser.first()
        assertEquals(latestData.id, newlocalUser.id)
        assertEquals(latestData.progress, newlocalUser.progress)
    }

    @Test
    fun whileObservingLocalUser_whenLearningSetsCountIsUpdated_thenLocalUserCountIsUpdated() = runTest {
        // While observing local user
        val localUser = User(LOCAL_USER_ID, HIMIKASE.id, 5)
        defaultUserRepository.updateLocalUserLearningSetsCount(localUser.learningSetsCount)
        val observedLocalUser = defaultUserRepository.observeLocalUser()

        // And local user is updated
        val newlocalUser = User(LOCAL_USER_ID, HIMIKASE.id, 1)
        defaultUserRepository.updateLocalUserLearningSetsCount(newlocalUser.learningSetsCount)

        // Then the observed user sends the updated values
        val latestData = observedLocalUser.first()
        assertEquals(latestData.id, newlocalUser.id)
        assertEquals(latestData.learningSetsCount, newlocalUser.learningSetsCount)
    }
}