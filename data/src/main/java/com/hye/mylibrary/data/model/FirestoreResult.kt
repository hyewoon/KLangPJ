package com.hye.data.firestore.model

sealed class FirebaseResult<out T> {
    data object DummyConstructor : FirebaseResult<Nothing>()
    data object Loading : FirebaseResult<Nothing>()
    data class Success<out T>(
        val data: T
    ) : FirebaseResult<T>()
    data class Failure(
        val exception: Exception
    ) : FirebaseResult<Nothing>()
}