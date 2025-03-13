package com.hye.domain.model

data class WordEntity(
    val targetCode: Int = 0,
    val frequency: Long? = 0L,
    val korean: String = "",
    val english: String = "",
    val pos: String = "",
    val wordGrade: String? = "",
    val exampleInfo: MutableList<WordExampleInfoEntity> = mutableListOf(),
    val pronunciationInfo: MutableList<WordPronunciationInfoEntity>? = mutableListOf()
)



data class WordExampleInfoEntity(
    val type: String = "",
    val example: String = ""
)

data class WordPronunciationInfoEntity(
    val pronunciation: String? = "",
    val audioUrl: String? = ""
)