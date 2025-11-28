package com.effatheresoft.mindlesslyhiragana.data

import com.effatheresoft.mindlesslyhiragana.Constants.LOCAL_USER_ID
import com.effatheresoft.mindlesslyhiragana.home.MainCoroutineRule
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
        progress = "1"
    )
    val localUserEntities = hashMapOf(localUserEntity.id to localUserEntity)

    override suspend fun upsertUser(user: UserRoomEntity) {
        localUserEntities[user.id] = user
    }

    override fun observeLocalUser(): Flow<UserRoomEntity> = flow {
        emit(localUserEntities[LOCAL_USER_ID] ?: localUserEntity)
    }

    override suspend fun getUserById(id: String): UserRoomEntity? {
        return localUserEntities[id]
    }

}

class DefaultUserRepositoryTest {
    private lateinit var defaultUserRepository: DefaultUserRepository

    @OptIn(ExperimentalCoroutinesApi::class)
    @get:Rule
    val mainCoroutineRule = MainCoroutineRule()

    @Before
    fun setupRepository() {
        defaultUserRepository = DefaultUserRepository(FakeUserDao())
    }

    @Test
    fun observedLocalUser_returnsLocalUser() = runTest {
        // When observing local user
        val localUser = User(LOCAL_USER_ID, "himikase")
        defaultUserRepository.setLocalUserProgress(localUser.progress)
        val observedLocalUser = defaultUserRepository.observeLocalUser()

        // And local user is updated
        val newlocalUser = User(LOCAL_USER_ID, "fuwoya")
        defaultUserRepository.setLocalUserProgress(newlocalUser.progress)

        // Then the observed user sends the updated values
        val latestData = observedLocalUser.first()
        assertEquals(latestData.id, newlocalUser.id)
        assertEquals(latestData.progress, newlocalUser.progress)
    }
}