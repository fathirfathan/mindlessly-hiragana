package com.effatheresoft.mindlesslyhiragana.data.repository

import com.effatheresoft.mindlesslyhiragana.data.local.UserDao
import com.effatheresoft.mindlesslyhiragana.data.local.toRoomEntityProgressOrNull
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

    suspend fun updateLocalUserProgress(progress: String) =
        progress.toRoomEntityProgressOrNull()?.let { roomEntityProgress ->
            localDataSource.updateLocalUserProgress(roomEntityProgress)
        }

    suspend fun continueLocalUserProgress() {
        getLocalUserOrNull()?.let { localUser ->
            val currentProgress = localUser.progress
            val nextHiraganaCategoryIndex = HiraganaCategory.entries.indexOfFirst { it.id == currentProgress } + 1
            val nextProgress = HiraganaCategory.entries.getOrNull(nextHiraganaCategoryIndex)?.id
            nextProgress?.let { updateLocalUserProgress(it) }
        }
    }

    suspend fun updateLocalUserLearningSetsCount(count: Int) =
        localDataSource.updateLocalUserLearningSetsCount(count)

    suspend fun updateLocalUserIsTestUnlocked(isUnlocked: Boolean) {
        localDataSource.updateLocalUserIsTestUnlocked(isUnlocked)
    }

    private suspend fun getLocalUserOrNull() = localDataSource.observeLocalUser().first().toUserOrNull()

    suspend fun lockTestAllLearned() {
        getLocalUserOrNull()?.let { localUser ->
            if (localUser.progress == HiraganaCategory.entries.last().id) return
            updateLocalUserIsTestUnlocked(false)
        }
    }

}