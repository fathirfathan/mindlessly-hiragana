package com.effatheresoft.mindlesslyhiragana.data

import com.effatheresoft.mindlesslyhiragana.Constants.DEFAULT_LEARNING_SETS_COUNT
import com.effatheresoft.mindlesslyhiragana.Constants.LOCAL_USER_ID
import com.effatheresoft.mindlesslyhiragana.data.HiraganaCategory.HIMIKASE
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map

class FakeUserRepository : UserRepository {

    private val _localUser = MutableStateFlow(User(LOCAL_USER_ID, HIMIKASE.id, DEFAULT_LEARNING_SETS_COUNT))
    val localUser = _localUser.asStateFlow()

    override fun observeLocalUser(): Flow<User> = localUser.map { it }

    override suspend fun updateLocalUserProgress(progress: String) {
        _localUser.value = _localUser.value.copy(progress = progress)
    }

    override suspend fun updateLocalUserLearningSetsCount(count: Int) {
        _localUser.value = _localUser.value.copy(learningSetsCount = count)
    }
}

