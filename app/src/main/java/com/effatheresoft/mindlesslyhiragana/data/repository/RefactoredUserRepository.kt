package com.effatheresoft.mindlesslyhiragana.data.repository

import com.effatheresoft.mindlesslyhiragana.data.local.UserDao
import com.effatheresoft.mindlesslyhiragana.data.local.toRoomEntityProgress
import com.effatheresoft.mindlesslyhiragana.data.local.toUser
import com.effatheresoft.mindlesslyhiragana.data.model.HiraganaCategory
import com.effatheresoft.mindlesslyhiragana.data.model.User
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class RefactoredUserRepository @Inject constructor(
    private val localDataSource: UserDao
) {
    fun observeLocalUser(): Flow<User> = localDataSource.observeLocalUser().map { it.toUser() }

    suspend fun updateLocalUserProgress(progress: String) =
        localDataSource.updateLocalUserProgress(progress.toRoomEntityProgress())

    suspend fun continueLocalUserProgress() {
        val currentProgress = localDataSource.observeLocalUser().first().toUser().progress
        val nextHiraganaCategoryIndex = HiraganaCategory.entries.indexOfFirst { it.id == currentProgress } + 1
        val nextProgress = HiraganaCategory.entries.getOrNull(nextHiraganaCategoryIndex)?.id
        nextProgress?.let { updateLocalUserProgress(it) }
    }

    suspend fun updateLocalUserLearningSetsCount(count: Int) =
        localDataSource.updateLocalUserLearningSetsCount(count)

    suspend fun updateLocalUserIsTestUnlocked(isUnlocked: Boolean) {
        localDataSource.updateLocalUserIsTestUnlocked(isUnlocked)
    }

    private suspend fun getLocalUser() = localDataSource.observeLocalUser().first().toUser()

    suspend fun lockTestAllLearned() {
        if (getLocalUser().progress == HiraganaCategory.entries.last().id) return

        updateLocalUserIsTestUnlocked(false)
    }

}