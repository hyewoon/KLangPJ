package com.hye.sesac.klangpj.common

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow


fun < T> Flow<T>.throttleFirst(intervalTime: Long) : Flow<T> = flow{
    var throttleTime = 0L
    collect { upFlow ->
        val currentTime = System.currentTimeMillis()
        if ((currentTime - throttleTime) > intervalTime) {
            throttleTime = currentTime
            emit(upFlow)
        }
    }
}
