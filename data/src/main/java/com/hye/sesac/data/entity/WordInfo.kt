package com.hye.sesac.data.entity

import com.tickaroo.tikxml.annotation.Element
import com.tickaroo.tikxml.annotation.PropertyElement
import com.tickaroo.tikxml.annotation.Xml




@Xml(name = "channel")
data class Channel(
    @PropertyElement(name= "total" )
        val total: Int = 0,
    @Element(name = "item")
    val item: List<WordInfo>? = null,
)

@Xml(name = "item")
data class WordInfo(
    @PropertyElement(name = "target_code")
    val targetCode: Int = 0,
    @PropertyElement(name = "word")
    val word: String = "",
    //품사
    @PropertyElement(name = "pos")
    val pos: String = "",
    @PropertyElement(name = "word_grade")
    val wordGrade : String="",
    @PropertyElement(name = "pronunciation")
    val pronunciation: String ="",
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
    val translation: Translation? = null
)

@Xml(name = "translation")
data class Translation(
    @PropertyElement(name = "trans_word")
    val transWord: String = "",
    @PropertyElement(name = "trans_dfn")
    val transDfn: String = "",
)
