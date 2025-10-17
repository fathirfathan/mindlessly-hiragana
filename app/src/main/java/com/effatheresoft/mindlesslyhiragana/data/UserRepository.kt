package com.effatheresoft.mindlesslyhiragana.data

import com.effatheresoft.mindlesslyhiragana.data.local.UserDao
import com.effatheresoft.mindlesslyhiragana.data.local.UserEntity
import com.effatheresoft.mindlesslyhiragana.data.local.toUser
import com.effatheresoft.mindlesslyhiragana.util.Result
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow

private const val simulatedDelay = 1_000L

class UserRepository(private val userDataSource: UserLocalDataSource) {
    fun getDefaultUser() = getUserById("1")

    fun getUserById(id: String): Flow<Result<User?>> = Result.flowWithResult {
        userDataSource.fetchUserById(id)
    }

    fun insertUser(user: User): Flow<Result<Boolean>> = Result.flowWithResult {
        userDataSource.insertUser(user)
    }
}

class UserLocalDataSource(private val userDao: UserDao) {
    suspend fun fetchUserById(id: String): User? {
        delay(simulatedDelay)
        return userDao.getUserById(id)?.toUser()
    }

    suspend fun insertUser(user: User): Boolean {
        delay(simulatedDelay)
        val insertedUserId = userDao.insert(user.toUserEntity())
        return insertedUserId != -1L
    }
}

data class UserInteractionHistory(
    val resetCount: Int
)

data class User(
    val id: String,
    val highestCategoryId: String,
//    val trainingSetCountsPreference: Int,
//    val interactionHistory: UserInteractionHistory
)

fun User.toUserEntity(): UserEntity = UserEntity(id, highestCategoryId)