package com.hye.domain.data

data class HandWritingPoint(
    val x: Float,
    val y: Float,
    val timestamp: Long
)

data class HandWritingStroke(
    val pointData: List<HandWritingPoint>
)



/*
data class RecognitionResult(
    val text: String,
    //정확도 0.0f ~ 1.0f 사이
    val confidence: Float,
    val isRecognizable: Boolean = confidence >= THRESHOLD

) {
    companion object{
        private const val THRESHOLD = 0.7f
    }
}*/
