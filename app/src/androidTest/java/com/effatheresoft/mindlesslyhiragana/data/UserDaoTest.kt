package com.effatheresoft.mindlesslyhiragana.data

import androidx.room.Room
import androidx.test.core.app.ApplicationProvider.getApplicationContext
import androidx.test.ext.junit.runners.AndroidJUnit4
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertNotNull
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class UserDaoTest {
    private lateinit var defaultDatabase: DefaultDatabase
    private lateinit var prepopulatedLocalUser: UserRoomEntity

    @Before
    fun initializeAndPrepopulateDatabase() = runTest {
        defaultDatabase = Room.inMemoryDatabaseBuilder(
            context = getApplicationContext(),
            klass = DefaultDatabase::class.java
        ).allowMainThreadQueries().build()

        prepopulatedLocalUser = UserRoomEntity(
            id = "localUser",
            progress = "1"
        )
        defaultDatabase.userDao().upsertUser(prepopulatedLocalUser)
    }

    @Test
    fun getLocalUser() = runTest {
        val loadedData = defaultDatabase.userDao().getLocalUser()
        assertNotNull(loadedData)
        assertEquals(loadedData.id, prepopulatedLocalUser.id)
        assertEquals(loadedData.progress, prepopulatedLocalUser.progress)
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