package com.effatheresoft.mindlesslyhiragana.data

import com.effatheresoft.mindlesslyhiragana.data.local.UserDao
import com.effatheresoft.mindlesslyhiragana.data.local.UserEntity
import com.effatheresoft.mindlesslyhiragana.data.local.toUser
import com.effatheresoft.mindlesslyhiragana.util.Result
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private const val simulatedDelay = 1_000L

class UserRepository(private val userDataSource: UserLocalDataSource) {
    suspend fun getDefaultUser() = getUserById("1")

    suspend fun getUserById(id: String): Flow<Result<User?>> = userDataSource.fetchUserById(id).map {
        if (it != null) {
            Result.Success(it.toUser())
        } else {
            Result.Success(null)
        }
    }

    suspend fun restartProgress() {
        userDataSource.updateUser(User(
            id = "1",
            highestCategoryId = "0",
            learningSetsCount = 3
        ))
    }

    fun insertUser(user: User): Flow<Result<Boolean>> = Result.flowWithResult {
        userDataSource.insertUser(user)
    }

    fun updateUser(user: User): Flow<Result<Boolean>> = Result.flowWithResult {
        userDataSource.updateUser(user)
    }
}

class UserLocalDataSource(private val userDao: UserDao) {
    suspend fun fetchUserById(id: String): Flow<UserEntity?> {
        delay(simulatedDelay)
        return userDao.getUserById(id)
    }

    suspend fun insertUser(user: User): Boolean {
        delay(simulatedDelay)
        val insertedUserId = userDao.insert(user.toUserEntity())
        return insertedUserId != -1L
    }

    suspend fun updateUser(user: User): Boolean {
        delay(simulatedDelay)
        val updatedRowsCount = userDao.update(user.toUserEntity())
        return updatedRowsCount > 0
    }
}

data class UserInteractionHistory(
    val resetCount: Int
)

data class User(
    val id: String,
    val highestCategoryId: String,
    val learningSetsCount: Int,
//    val interactionHistory: UserInteractionHistory
)

fun User.toUserEntity(): UserEntity = UserEntity(id, highestCategoryId, learningSetsCount)