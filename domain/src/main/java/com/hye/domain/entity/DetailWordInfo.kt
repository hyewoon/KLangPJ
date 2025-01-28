package com.hye.sesac.data.entity

import com.tickaroo.tikxml.annotation.Element
import com.tickaroo.tikxml.annotation.PropertyElement
import com.tickaroo.tikxml.annotation.Xml

@Xml(name = "channel")
data class DetailChannel(
    @Element(name = "item")
    val items: List<DetailItem>? = listOf(),
)

@Xml(name = "item")
data class DetailItem(
    @PropertyElement(name = "target_code")
    val targetCode: Int = 0,
    @Element(name = "word_info")
    val wordInfo: TargetWordInfo? = TargetWordInfo(),
)

@Xml(name = "word_info")
data class TargetWordInfo(
    @PropertyElement(name = "word")
    val word: String = "",
    @PropertyElement(name = "pos")
    val pos: String = "",
    @PropertyElement(name = "word_grade")
    val wordGrade: String = "",
    @Element(name = "pronunciation_info")
    val pronunciationInfo: List<PronunciationInfo>? = listOf(),
    @Element(name = "sense_info")
    val senseInfo: List<SenseInfo>? = listOf(),


    )

@Xml(name = "pronunciation_info")
data class PronunciationInfo(
    @PropertyElement(name = "pronunciation")
   val pronunciation: String = "",
    @PropertyElement(name = "link")
    val link: String?="",
)

@Xml(name = "sense_info")
data class SenseInfo(
    @PropertyElement(name = "definition")
    val definition: String = "",
    @Element(name = "example_info")
    val exampleInfo: List<ExampleInfo>? = listOf(),
)

@Xml(name = "example_info")
data class ExampleInfo(
    @PropertyElement(name = "type")
    val type: String = "",
    @PropertyElement(name = "example")
    val example: String = "",
)
