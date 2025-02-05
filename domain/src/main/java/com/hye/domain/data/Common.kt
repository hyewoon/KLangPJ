package com.hye.domain.data

sealed class MLKitResult<out T>{
    data object Initial: MLKitResult<Nothing>()
    data class Success<out T>(val data: T): MLKitResult<T>()
    data class Failure(val exception: String): MLKitResult<Nothing>()
}


