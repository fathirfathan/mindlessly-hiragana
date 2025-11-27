package com.effatheresoft.mindlesslyhiragana.data

import jakarta.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map

class DefaultUserRepository @Inject constructor(): UserRepository {

    private val _localUser = MutableStateFlow(User("localUser", "himikase"))
    val localUser = _localUser.asStateFlow()

    override fun getLocalUser(): Flow<User> = localUser.map { it }

    override suspend fun setLocalUserProgress(string: String) {
        _localUser.value = _localUser.value.copy(progress = string)
    }
}