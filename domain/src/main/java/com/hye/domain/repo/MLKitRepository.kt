package com.hye.domain.repo

import com.hye.domain.data.HandWritingStroke
import com.hye.domain.data.MLKitResult


interface MLKitHandwritingRecognizer {
    suspend fun recognize(strokes : List<HandWritingStroke>) : MLKitResult<String>

    fun cleanUp()
}