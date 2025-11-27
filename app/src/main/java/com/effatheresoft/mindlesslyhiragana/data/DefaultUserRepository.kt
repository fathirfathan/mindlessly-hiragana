package com.effatheresoft.mindlesslyhiragana.data

import androidx.compose.runtime.mutableStateOf
import jakarta.inject.Inject
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class DefaultUserRepository @Inject constructor(): UserRepository {
    val localUser = mutableStateOf(User("localUser", "himikase"))

    override fun getLocalUser(): Flow<User> = flow {
        delay(1000)
        emit(localUser.value)
    }
}