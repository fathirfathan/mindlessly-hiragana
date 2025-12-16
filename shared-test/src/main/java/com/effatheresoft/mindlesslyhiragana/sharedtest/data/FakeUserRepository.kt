package com.effatheresoft.mindlesslyhiragana.data

import com.effatheresoft.mindlesslyhiragana.Constants.DEFAULT_LEARNING_SETS_COUNT
import com.effatheresoft.mindlesslyhiragana.Constants.LOCAL_USER_ID
import com.effatheresoft.mindlesslyhiragana.data.model.HiraganaCategory.HIMIKASE
import com.effatheresoft.mindlesslyhiragana.data.model.User
import com.effatheresoft.mindlesslyhiragana.data.repository.UserRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update

class FakeUserRepository : UserRepository {

    private val _localUser = MutableStateFlow(
        User(
            LOCAL_USER_ID,
            HIMIKASE.id,
            DEFAULT_LEARNING_SETS_COUNT,
            false
        )
    )
    override fun observeLocalUser(): Flow<User> = _localUser

    override suspend fun updateLocalUserProgress(progress: String) {
        _localUser.update { it.copy(progress = progress) }
    }

    override suspend fun updateLocalUserLearningSetsCount(count: Int) {
        _localUser.update { it.copy(learningSetsCount = count) }
    }

    override suspend fun updateLocalUserIsTestUnlocked(isUnlocked: Boolean) {
        _localUser.update { it.copy(isTestUnlocked = isUnlocked) }
    }
}

