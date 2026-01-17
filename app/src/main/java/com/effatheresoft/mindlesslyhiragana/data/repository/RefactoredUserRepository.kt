package com.effatheresoft.mindlesslyhiragana.data.repository

import com.effatheresoft.mindlesslyhiragana.data.local.UserDao
import com.effatheresoft.mindlesslyhiragana.data.local.toUserOrNull
import com.effatheresoft.mindlesslyhiragana.data.model.HiraganaCategory
import com.effatheresoft.mindlesslyhiragana.data.model.User
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.mapNotNull
import javax.inject.Inject

class RefactoredUserRepository @Inject constructor(
    private val localDataSource: UserDao
) {
    fun observeLocalUser(): Flow<User> = localDataSource.observeLocalUser().mapNotNull { it.toUserOrNull() }

    suspend fun updateLocalUserProgress(progress: HiraganaCategory) =
        localDataSource.updateLocalUserProgress(progress.toRoomEntityProgress())
    suspend fun continueLocalUserProgress() {
        getLocalUserProgressOrNull()?.let { progress ->
            updateLocalUserProgress(progress.getNextCategoryOrNull() ?: progress)
        }
    }

    suspend fun updateLocalUserLearningSetsCount(count: Int) =
        localDataSource.updateLocalUserLearningSetsCount(count)

    suspend fun updateLocalUserIsTestUnlocked(isUnlocked: Boolean) {
        localDataSource.updateLocalUserIsTestUnlocked(isUnlocked)
    }

    private suspend fun getLocalUserProgressOrNull() = localDataSource.observeLocalUser().first().toUserOrNull()?.progress

    suspend fun lockTestAllLearned() {
        getLocalUserProgressOrNull()?.let { progress ->
            if (progress.isLastCategory) return

            updateLocalUserIsTestUnlocked(false)
        }
    }
}