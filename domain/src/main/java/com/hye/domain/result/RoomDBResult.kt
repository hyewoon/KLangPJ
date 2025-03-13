package com.hye.domain.result

sealed class RoomDBResult <out T>{
    data object NoConstructor: RoomDBResult<Nothing>()
    data object Loading: RoomDBResult<Nothing>()
    data class Success<out T>(val data: T): RoomDBResult<T>()
    data class RoomDBError(val exception:Throwable ): RoomDBResult<Nothing>()
}