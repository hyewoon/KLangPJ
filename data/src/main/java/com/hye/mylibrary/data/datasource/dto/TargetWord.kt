package com.hye.mylibrary.data.datasource.dto

data class WordDto (
        val targetCode: Int = 0,
        val frequency: Long? = 0L,
        val korean: String ="",
        val english: String = "",
        val pos: String= "",
        val wordGrade: String? = "",
        val exampleInfo: MutableList<WordExampleInfo> = mutableListOf(),
        val pronunciationInfo: MutableList<com.hye.mylibrary.data.datasource.dto.WordPronunciationInfo>? = mutableListOf()
        )

data class WordExampleInfo(
    val type: String ="",
    val example: String=""
)

data class WordPronunciationInfo(
    val pronunciation: String? = "",
    val audioUrl: String? = ""
)