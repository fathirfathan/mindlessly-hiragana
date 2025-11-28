package com.effatheresoft.mindlesslyhiragana.data

import com.effatheresoft.mindlesslyhiragana.Constants.LOCAL_USER_ID
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map

class FakeUserRepository : UserRepository {

    private val _localUser = MutableStateFlow(User(LOCAL_USER_ID, "himikase"))
    val localUser = _localUser.asStateFlow()

    override fun observeLocalUser(): Flow<User> = localUser.map { it }

    override suspend fun setLocalUserProgress(string: String) {
        _localUser.value = _localUser.value.copy(progress = string)
    }
}

