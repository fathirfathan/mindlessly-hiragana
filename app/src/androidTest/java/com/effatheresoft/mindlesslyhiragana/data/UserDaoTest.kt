package com.effatheresoft.mindlesslyhiragana.data
//
//import androidx.room.Room
//import androidx.test.core.app.ApplicationProvider.getApplicationContext
//import androidx.test.ext.junit.runners.AndroidJUnit4
//import com.effatheresoft.mindlesslyhiragana.Constants.LOCAL_USER_ID
//import com.effatheresoft.mindlesslyhiragana.data.local.DefaultDatabase
//import com.effatheresoft.mindlesslyhiragana.data.local.UserRoomEntity
//import junit.framework.TestCase.assertEquals
//import junit.framework.TestCase.assertNotNull
//import kotlinx.coroutines.flow.Flow
//import kotlinx.coroutines.flow.first
//import kotlinx.coroutines.test.runTest
//import org.junit.Before
//import org.junit.Test
//import org.junit.runner.RunWith
//
//@RunWith(AndroidJUnit4::class)
//class UserDaoTest {
//    private lateinit var defaultDatabase: DefaultDatabase
//
//    @Before
//    fun initializeDatabase() = runTest {
//        defaultDatabase = Room.inMemoryDatabaseBuilder(
//            context = getApplicationContext(),
//            klass = DefaultDatabase::class.java
//        ).allowMainThreadQueries().build()
//
//        val localUser = UserRoomEntity(
//            id = LOCAL_USER_ID,
//            progress = "1",
//            learningSetsCount = 5,
//            isTestUnlocked = false
//        )
//        defaultDatabase.userDao().upsertUser(localUser)
//    }
//
//    @Test
//    fun newUser_whenIsInserted_assertIsInserted() = runTest {
//        // GIVEN - A new user is inserted
//        val newUser = UserRoomEntity(
//            id = "newUser",
//            progress = "1",
//            learningSetsCount = 5,
//            isTestUnlocked = false
//        )
//        defaultDatabase.userDao().upsertUser(newUser)
//
//        // THEN - The loaded data contains the expected values
//        val loadedData = defaultDatabase.userDao().getUserById(newUser.id)
//        assertNotNull(loadedData as UserRoomEntity)
//        assertEquals(loadedData.id, newUser.id)
//        assertEquals(loadedData.progress, newUser.progress)
//        assertEquals(loadedData.learningSetsCount, newUser.learningSetsCount)
//        assertEquals(loadedData.isTestUnlocked, newUser.isTestUnlocked)
//    }
//
//    @Test
//    fun newLocalUserData_whenIsUpdated_newData_assertIsObserved() = runTest {
//        // Observe local user
//        val observedLocalUser: Flow<UserRoomEntity> = defaultDatabase.userDao().observeLocalUser()
//
//        // When local user is updated
//        val updatedLocalUser = UserRoomEntity(
//            id = LOCAL_USER_ID,
//            progress = "2",
//            learningSetsCount = 1,
//            isTestUnlocked = false
//        )
//        defaultDatabase.userDao().upsertUser(updatedLocalUser)
//
//        // Then the observed user sends the updated values
//        val latestData = observedLocalUser.first()
//        assertEquals(latestData.id, updatedLocalUser.id)
//        assertEquals(latestData.progress, updatedLocalUser.progress)
//        assertEquals(latestData.learningSetsCount, updatedLocalUser.learningSetsCount)
//        assertEquals(latestData.isTestUnlocked, updatedLocalUser.isTestUnlocked)
//    }
//
//    @Test
//    fun localUser_progress_whenIsUpdated_progress_assertIsUpdated() = runTest {
//        val updatedProgress = "2" // HiraganaCategory.FUWOYA
//        defaultDatabase.userDao().updateLocalUserProgress(updatedProgress)
//
//        val loadedData = defaultDatabase.userDao().getUserById(LOCAL_USER_ID)
//        assertNotNull(loadedData as UserRoomEntity)
//        assertEquals(updatedProgress, loadedData.progress)
//    }
//
//    @Test
//    fun localUser_learningSetsCount_whenIsUpdated_count_assertIsUpdated() = runTest {
//        val updatedLearningSetsCount = 6
//        defaultDatabase.userDao().updateLocalUserLearningSetsCount(updatedLearningSetsCount)
//
//        val loadedData = defaultDatabase.userDao().getUserById(LOCAL_USER_ID)
//        assertNotNull(loadedData as UserRoomEntity)
//        assertEquals(updatedLearningSetsCount, loadedData.learningSetsCount)
//    }
//
//    @Test
//    fun localUser_isTestUnlocked_whenIsUpdated_isUnlocked_assertIsUpdated() = runTest {
//        val updatedIsTestUnlocked = true
//        defaultDatabase.userDao().updateLocalUserIsTestUnlocked(updatedIsTestUnlocked)
//
//        val loadedData = defaultDatabase.userDao().getUserById(LOCAL_USER_ID)
//        assertNotNull(loadedData as UserRoomEntity)
//        assertEquals(updatedIsTestUnlocked, loadedData.isTestUnlocked)
//    }
//}