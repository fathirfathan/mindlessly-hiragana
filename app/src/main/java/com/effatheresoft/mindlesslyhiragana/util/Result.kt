package com.effatheresoft.mindlesslyhiragana.util

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow

sealed class Result<out T> {
    data object Loading : Result<Nothing>()
    data class Success<out T>(val data: T) : Result<T>()
    data class Error(val exception: Throwable) : Result<Nothing>()

    companion object {
        fun <T> flowWithResult(block: suspend () -> T): Flow<Result<T>> = flow {
            emit(Loading)
            val result = block()
            emit(Success(result))
        }.catch { e ->
            emit(Error(e))
        }
    }
}