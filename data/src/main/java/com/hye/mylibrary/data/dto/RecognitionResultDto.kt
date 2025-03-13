package com.hye.mylibrary.data.dto

data class RecognitionResultDto(
    val recognizedText: String,
    //신뢰도 0.0 ~ 1.0사이
    val confidence: Float
)

