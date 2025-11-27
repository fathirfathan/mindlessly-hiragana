package com.effatheresoft.mindlesslyhiragana.data

import kotlinx.coroutines.flow.Flow


interface UserRepository {
    fun getLocalUser(): Flow<User>
}