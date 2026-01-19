package com.effatheresoft.mindlesslyhiragana.data.repository

import com.effatheresoft.mindlesslyhiragana.data.local.LocalUserDao
import com.effatheresoft.mindlesslyhiragana.data.local.toUserOrNull
import com.effatheresoft.mindlesslyhiragana.data.model.HiraganaCategory
import com.effatheresoft.mindlesslyhiragana.data.model.User
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.mapNotNull
import javax.inject.Inject

class UserRepository @Inject constructor(
    private val localDataSource: LocalUserDao
) {
    fun observeUser(): Flow<User> = localDataSource.observeLocalUser().mapNotNull { it.toUserOrNull() }

    suspend fun updateHighestCategory(category: HiraganaCategory) =
        localDataSource.updateHighestCategory(category.toRoomEntityCategory())
    suspend fun advanceHighestCategory() {
        getHighestCategoryOrNull()?.let { category ->
            updateHighestCategory(category.getNextCategoryOrNull() ?: category)
        }
    }

    suspend fun updateRepeatCategoryCount(count: Int) =
        localDataSource.updateRepeatCategoryCount(count)

    suspend fun updateIsTestUnlocked(isUnlocked: Boolean) {
        localDataSource.updateIsTestUnlocked(isUnlocked)
    }

    private suspend fun getHighestCategoryOrNull() = localDataSource.observeLocalUser().first().toUserOrNull()?.highestCategory

    suspend fun lockTestAllLearned() {
        getHighestCategoryOrNull()?.let { category ->
            if (category.isLastCategory) return

            updateIsTestUnlocked(false)
        }
    }
    suspend fun unlockTestAllLearned() {
        updateIsTestUnlocked(true)
    }
}