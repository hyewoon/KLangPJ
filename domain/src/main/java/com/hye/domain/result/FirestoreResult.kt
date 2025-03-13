package com.hye.domain.result

sealed class FirebaseResult<out T> {
    data object DummyConstructor : FirebaseResult<Nothing>()
    data object Loading : FirebaseResult<Nothing>()
    data class Success<out T>(
        val data: T
    ) : FirebaseResult<T>()
    data class NetWorkFailure(
        val exception: Throwable
    ) : FirebaseResult<Nothing>()
    data class RoomDBFailure(
        val exception: Throwable
    ) : FirebaseResult<Nothing>()

}