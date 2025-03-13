package com.hye.mylibrary.data.datasource.dto

import com.google.firebase.firestore.PropertyName

/**
 * firestore에서 받아온 데이터 파싱
 * example은 3개로 갯수 제한
 */
data class TargetWordDto (
    var targetCode: Long = 0L,
    var frequency: Long? = 0L,
    var korean: String ="",
    var english: String = "",
    var pos: String= "",
    var wordGrade: String? = "",
    @PropertyName("example_info")
        var exampleInfo: List<TargetWordExampleInfoDto>? = emptyList(),
    @PropertyName("pronunciation_info")
        var pronunciationInfo: List<TargetWordPronunciationInfoDto>? = emptyList()
        ){
    //example 정보중 필요하 갯수만 filter
    fun getFilteredExamples(type:String, limit:Int): List<TargetWordExampleInfoDto> {
        return exampleInfo?.filter { it.type == type }?.take(limit) ?: emptyList()
    }
}


data class TargetWordExampleInfoDto(
    @PropertyName("type")
    var type: String ="" ,
    @PropertyName("example")
    var example: String=""
)

data class TargetWordPronunciationInfoDto(
    @PropertyName("pronunciation_info.pronunciation")
    var pronunciation: String? = "",
    @PropertyName("pronunciation_info.link")
    var audioUrl: String? = ""
)