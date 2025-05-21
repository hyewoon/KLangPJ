package com.hye.sesac.klangpj.state

sealed class UiStateResult<out T>{
    data  object Loading: UiStateResult<Nothing>()
    data class Success<T>(val data: T): UiStateResult<T>()
    data class NetWorkFailure(val error: String): UiStateResult<Nothing>()
    data class RoomDBFailure(val error: String): UiStateResult<Nothing>()
}
