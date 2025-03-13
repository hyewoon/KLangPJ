package com.hye.domain.result

sealed class ApiResult<out T> {
    data object DummyConstructor : ApiResult<Nothing>()
    data object Loading : ApiResult<Nothing>()
    data class Success<out T>(
        val data: T
    ) : ApiResult<T>()
    data class Failure(val message: String) : ApiResult<Nothing>()
}