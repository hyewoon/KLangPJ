package com.hye.domain.model

data class HandWritingPoint(
    val x: Float,
    val y: Float,
    val timestamp: Long
)

data class HandWritingStroke(
    val pointData: List<HandWritingPoint>
)


data class MLKitRecognitionResult(
    val recognizedText: String,
    val confidence: Float,
    val isValidRecognition: Boolean = confidence >= -1000
)