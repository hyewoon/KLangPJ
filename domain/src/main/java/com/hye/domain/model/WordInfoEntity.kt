package com.hye.domain.model

/*

data class ChannelEntity(
    val item: List<WordInfoEntity>?= null
)
*/

/*data class WordInfoEntity(
    val targetCode: Int =0,
    val word: String="",
    val supNo: Int = 0,
    val wordGrade: String? = "",
    val pos: String="",
    val sense: List<SenseEntity> = mutableListOf()
)

data class SenseEntity(
    val senseOrder: Int = 0,
    val definition: String = "",
    val translation: TranslationEntity? = TranslationEntity()
)


data class TranslationEntity(
    val transWord: String = "",
    val transDfn: String = ""
)*/

data class SenseInfo(
    val senseOrder: Int = 0,
    val transWord: String = "",
    val definition: String = "",
    val transDfn: String = "",
)

data class WordEntity(
    val targetCode: Int = 0,
    val word: String = "",
    val supNo: Int = 0,
    val wordGrade: String? = "",
    val pos: String = "",
    val senses: List<SenseInfo> = mutableListOf()


    )
