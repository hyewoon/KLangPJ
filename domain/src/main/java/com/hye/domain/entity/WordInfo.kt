package com.hye.sesac.data.entity
import com.tickaroo.tikxml.annotation.Element
import com.tickaroo.tikxml.annotation.PropertyElement
import com.tickaroo.tikxml.annotation.Xml



@Xml(name = "channel")
data class Channel(
    @Element(name = "item")
    val item: List<WordInfo>?= null
)

@Xml(name = "item")
data class WordInfo(
    @PropertyElement(name = "target_code")
    val targetCode: Int =0,

    @PropertyElement(name = "word")
    val word: String="",

    @PropertyElement(name = "sup_no")
    val supNo: Int = 0,

   /* @PropertyElement(name = "pronunciation", writeAsCData = true)
    val pronunciation: String? = "",*/

    @PropertyElement(name = "word_grade")
    val wordGrade: String? = "",

    //품사
    @PropertyElement(name = "pos")
    val pos: String="",

   /* @PropertyElement(name = "link")
    val link: String="",
*/
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
   /* @PropertyElement(name="trans_lang")
    val tranLang: String="",*/
    @PropertyElement(name = "trans_word")
    val transWord: String = "",
    @PropertyElement(name = "trans_dfn")
    val transDfn: String = ""
)
