package com.effatheresoft.mindlesslyhiragana.data

import android.util.Log
import com.effatheresoft.mindlesslyhiragana.data.local.UserDao
import com.effatheresoft.mindlesslyhiragana.data.local.UserEntity
import com.effatheresoft.mindlesslyhiragana.data.local.UserInteractionEntity
import com.effatheresoft.mindlesslyhiragana.data.local.UserWithInteractions
import com.effatheresoft.mindlesslyhiragana.data.local.toUser
import com.effatheresoft.mindlesslyhiragana.util.Result
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.serialization.Serializable

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
        userDataSource.updateUser(
            User(
                id = "1",
                highestCategoryId = "0",
                learningSetsCount = 3,
                userInteractions = emptyList()
            )
        )
    }

    fun insertUser(user: User): Flow<Result<Boolean>> = Result.flowWithResult {
        userDataSource.insertUser(user)
    }

    fun updateUser(user: User): Flow<Result<Boolean>> = Result.flowWithResult {
        userDataSource.updateUser(user)
    }

    suspend fun recordInteraction(interaction: UserInteraction) {
        Log.d("UserRepository", "$interaction")
        userDataSource.insertInteraction(interaction.toUserInteractionEntity("1"))
    }
}

class UserLocalDataSource(private val userDao: UserDao) {
    suspend fun fetchUserById(id: String): Flow<UserWithInteractions?> {
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

    suspend fun insertInteraction(interaction: UserInteractionEntity) {
        userDao.insertInteraction(interaction)
    }

    suspend fun clearInteractions(userId: String) {
        userDao.clearInteractions(userId)
    }
}

@Serializable
data class UserInteraction(
    val timestamp: String,
    val event: String,
    val target: String
)

fun UserInteraction.toUserInteractionEntity(userId: String): UserInteractionEntity {
    return UserInteractionEntity(userId = userId, timestamp = timestamp, event = event, target = target)
}

data class User(
    val id: String,
    val highestCategoryId: String = "0",
    val learningSetsCount: Int = 3,
    val userInteractions: List<UserInteraction> = emptyList()
)

fun User.toUserEntity(): UserEntity = UserEntity(
    id = id,
    highestCategoryId = highestCategoryId,
    learningSetsCount = learningSetsCount,
)
