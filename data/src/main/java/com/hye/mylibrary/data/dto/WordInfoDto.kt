package com.hye.mylibrary.data.dto
import com.tickaroo.tikxml.annotation.Element
import com.tickaroo.tikxml.annotation.PropertyElement
import com.tickaroo.tikxml.annotation.Xml



@Xml(name = "channel")
data class Channel(
    @Element(name = "item")
    val item: List<WordInfo>?= mutableListOf()
)

@Xml(name = "item")
data class WordInfo(
    @PropertyElement(name = "target_code")
    val targetCode: Int =0,

    @PropertyElement(name = "word")
    val word: String="",

    @PropertyElement(name = "sup_no")
    val supNo: Int = 0,


    @PropertyElement(name = "word_grade")
    val wordGrade: String? = "",

    //품사
    @PropertyElement(name = "pos")
    val pos: String="",

    @Element(name = "sense")
    val sense: List<Sense> = listOf()
)

@Xml(name = "sense")
data class Sense(
    @PropertyElement(name = "sense_order")
    val senseOrder: Int = 0,
    @PropertyElement(name = "definition")
    val definition: String = "",
    @Element(name = "translation")
    val translation: Translation? = Translation()
)

@Xml(name = "translation")
data class Translation(
    @PropertyElement(name = "trans_word")
    val transWord: String = "",
    @PropertyElement(name = "trans_dfn")
    val transDfn: String = ""
)
