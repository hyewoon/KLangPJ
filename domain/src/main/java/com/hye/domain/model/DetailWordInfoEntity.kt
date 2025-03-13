 package com.hye.domain.model


/*data class DetailChannelEntity(
    val items: List<DetailItemEntity>? = listOf(),
)

data class DetailItemEntity(
    val targetCode: Int = 0,
    val wordInfo: TargetWordInfoEntity? = TargetWordInfoEntity(),
)

data class TargetWordInfoEntity(
    val word: String = "",
    val pos: String = "",
    val wordGrade: String = "",
    val pronunciationInfo: List<PronunciationInfoEntity>? = listOf(),
    val senseInfo: List<SenseInfoEntity>? = listOf(),
    )

data class PronunciationInfoEntity(
    val pronunciation: String = "",
    val link: String?="",
)

data class SenseInfoEntity(
    val definition: String = "",
    val exampleInfo: List<ExampleInfoEntity>? = listOf(),
)

data class ExampleInfoEntity(
    val type: String = "",
    val example: String = "",
)*/

 data class ExampleInfo(
     val example: String = "",
     val type: String = "",
 )


 data class DetailWordEntity(
     val targetCode: Int = 0,
     val word: String = "",
     val pos: String = "",
     val wordGrade: String = "",
     val pronunciation: String? ="",
     val pronunciationLink: String? ="",
     val senses : List<SenseInfo> = mutableListOf(),
     val examples : List<ExampleInfo> = mutableListOf()

     )