package com.hye.domain.model

data class TargetWordEntity(
    val targetCode: Long = 0L,
    val frequency: Long? = 0L,
    val korean: String = "",
    val english: String = "",
    val pos: String = "",
    val wordGrade: String? = "",
    val exampleInfo: MutableList<TargetWordExampleInfoEntity>? = mutableListOf(),
    val pronunciationInfo: MutableList<TargetWordPronunciationInfoEntity>? = mutableListOf()
)


data class TargetWordExampleInfoEntity(
    val type: String = "",
    val example: String = ""
)


data class TargetWordPronunciationInfoEntity(
    val pronunciation: String? = "",
    val audioUrl: String? = ""
)