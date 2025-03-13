package com.hye.domain.repo

import com.hye.domain.model.HandWritingStroke
import com.hye.domain.model.MLKitRecognitionResult
import com.hye.domain.result.MLKitResult
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharedFlow


interface MLKitRepository {

    /*val resultFlow: SharedFlow<MLKitResult<String>>
    suspend fun recognize(strokes : List<HandWritingStroke>)

    fun cleanUp()*/

    suspend fun recognize(strokes: List<HandWritingStroke>) : Flow<MLKitResult<String>>
    fun cleanUp()
}