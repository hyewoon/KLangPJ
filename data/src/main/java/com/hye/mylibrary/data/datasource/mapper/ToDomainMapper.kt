package com.hye.mylibrary.data.datasource.mapper

import com.hye.domain.model.TargetWordWithAllInfoEntity
import com.hye.domain.model.WordExampleInfoEntity
import com.hye.domain.model.WordPronunciationInfoEntity
import com.hye.mylibrary.data.room.TargetWordWithAllInfo

class ToDomainMapper {
    fun mapToDomain(room: TargetWordWithAllInfo): TargetWordWithAllInfoEntity {
        return TargetWordWithAllInfoEntity(
            targetCode = room.targetWord.targetCode,
            frequency = room.targetWord.frequency,
            korean = room.targetWord.korean,
            english = room.targetWord.english,
            wordGrade = room.targetWord.wordGrade,
            pos = room.targetWord.pos,
            timeStamp = room.targetWord.timeStamp,
            todayString = room.targetWord.todayString,
            exampleInfo = room.exampleInfo.map {
                WordExampleInfoEntity(
                    type = it.type,
                    example = it.example
                )
            },
            pronunciationInfo = room.pronunciationInfo.map {
                WordPronunciationInfoEntity(
                    pronunciation = it.pronunciation,
                    audioUrl = it.audioUrl
                )


            }
        )
    }
}