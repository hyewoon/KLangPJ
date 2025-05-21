package com.hye.mylibrary.data.mapper


import com.hye.domain.model.DetailWordEntity
import com.hye.domain.model.ExampleInfo
import com.hye.mylibrary.data.dto.DetailItem
import com.hye.domain.model.SenseInfo
import com.hye.mylibrary.data.dto.WordInfo


class DetailWordInfoMapper {

    fun mapToDomain(item: DetailItem): DetailWordEntity {
        val word = WordInfo()
        return DetailWordEntity(
                targetCode = item.targetCode,
            word = item.wordInfo.word?:"",
            pos = item.wordInfo.pos?:"",
            wordGrade = item.wordInfo.wordGrade?:"",
            pronunciation = item.wordInfo.pronunciationInfo.joinToString {
                it.pronunciation
            },
            pronunciationLink = item.wordInfo.pronunciationInfo.joinToString {
                it.link ?: ""
            },
            senses = word.sense.map {
                SenseInfo(
                    senseOrder = it.senseOrder,
                    definition = it.definition,
                    transWord = it.translation.transWord?:"",
                    transDfn = it.translation.transDfn?:""
                )
            },

           examples = item.wordInfo.senseInfo.flatMap {
               it.exampleInfo.map { example->
                   ExampleInfo(
                       example = example.example,
                       type = example.type
                   )
               } ?: emptyList()
           } ?: emptyList()


        )
    }
}