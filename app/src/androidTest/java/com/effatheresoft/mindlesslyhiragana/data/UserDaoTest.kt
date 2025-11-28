package com.effatheresoft.mindlesslyhiragana.data

import androidx.room.Room
import androidx.test.core.app.ApplicationProvider.getApplicationContext
import androidx.test.ext.junit.runners.AndroidJUnit4
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertNotNull
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class UserDaoTest {
    private lateinit var defaultDatabase: DefaultDatabase
    private val localUserId = "localUser"

    @Before
    fun initializeDatabase() = runTest {
        defaultDatabase = Room.inMemoryDatabaseBuilder(
            context = getApplicationContext(),
            klass = DefaultDatabase::class.java
        ).allowMainThreadQueries().build()
    }

    @Test
    fun observedLocalUser_sendsNewData_whenLocalUserChanges() = runTest {
        // Observe local user
        val localUser = UserRoomEntity(
            id = localUserId,
            progress = "1"
        )
        defaultDatabase.userDao().upsertUser(localUser)
        val observedLocalUser: Flow<UserRoomEntity> = defaultDatabase.userDao().observeLocalUser()

        // When local user is updated
        val updatedLocalUser = UserRoomEntity(
            id = localUserId,
            progress = "2"
        )
        defaultDatabase.userDao().upsertUser(updatedLocalUser)

        // Then the observed user sends the updated values
        val latestData = observedLocalUser.first()
        assertEquals(latestData.id, updatedLocalUser.id)
        assertEquals(latestData.progress, updatedLocalUser.progress)
    }

    @Test
    fun insertNewUser_ThenGetUserById() = runTest {
        // GIVEN - A new user is inserted
        val newUser = UserRoomEntity(
            id = "newUser",
            progress = "1"
        )
        defaultDatabase.userDao().upsertUser(newUser)

        // THEN - The loaded data contains the expected values
        val loadedData = defaultDatabase.userDao().getUserById(newUser.id)
        assertNotNull(loadedData as UserRoomEntity)
        assertEquals(loadedData.id, newUser.id)
        assertEquals(loadedData.progress, newUser.progress)
    }
}