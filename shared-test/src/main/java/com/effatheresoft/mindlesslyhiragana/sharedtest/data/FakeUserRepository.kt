package com.effatheresoft.mindlesslyhiragana.data

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map

class FakeUserRepository : UserRepository {

    private val _localUser = MutableStateFlow(User("localUser", "himikase"))
    val localUser = _localUser.asStateFlow()

    override fun getLocalUser(): Flow<User> = localUser.map { it }
}

