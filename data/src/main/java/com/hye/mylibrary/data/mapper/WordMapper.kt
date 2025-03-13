package com.hye.mylibrary.data.mapper


import com.hye.domain.model.SenseInfo
import com.hye.domain.model.WordEntity
import com.hye.mylibrary.data.dto.WordInfo

class WordMapper {

    fun mapToDomain(wordInfo: WordInfo): WordEntity {
        return WordEntity(
            targetCode = wordInfo.targetCode,
            word = wordInfo.word,
            supNo = wordInfo.supNo,
            wordGrade = wordInfo.wordGrade,
            pos = wordInfo.pos,
            senses = wordInfo.sense.map {
                SenseInfo(
                    senseOrder = it.senseOrder,
                    definition = it.definition,
                    transWord = it.translation?.transWord ?: "",
                    transDfn = it.translation?.transDfn ?: ""
                )

            }

        )
    }


}