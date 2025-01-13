package com.hye.sesac.klangpj.data

sealed class TextRecognitionResult {
    data class Success(val text: String): TextRecognitionResult()
    data class Fail(val message: String): TextRecognitionResult()

}