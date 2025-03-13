package com.hye.mylibrary.data.mapper

import com.hye.domain.model.MLKitRecognitionResult
import com.hye.mylibrary.data.dto.RecognitionResultDto

class RecognitionMapper {

    fun mapToDomain( recognition: RecognitionResultDto): MLKitRecognitionResult {
        return MLKitRecognitionResult(
            recognizedText = recognition.recognizedText,
            confidence = recognition.confidence
        )


    }
}