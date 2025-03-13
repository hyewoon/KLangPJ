package com.hye.mylibrary.data.datasource.mapper

import com.hye.domain.model.TargetWordEntity
import com.hye.domain.model.TargetWordExampleInfoEntity
import com.hye.domain.model.TargetWordPronunciationInfoEntity

/**
 * firestore에서 받아온 정보를 domain으로 변환하는 mapper
 * firestore에서 받아온 정보를 roomdb에 저장하도록 변환하는 mapper
 *
 */
class TargetWordMapper {
    /**
     * to domain
     */
    fun mapToDomain(dto: TargetWordEntity): TargetWordEntity {
        return TargetWordEntity(
            targetCode = dto.targetCode,
            frequency = dto.frequency,
            korean = dto.korean,
            english = dto.english,
            pos = dto.pos,
            wordGrade = dto.wordGrade,
            exampleInfo = dto.exampleInfo.map { mapExampleInfoToDomain(it) } as MutableList<TargetWordExampleInfoEntity>,
            pronunciationInfo = dto.pronunciationInfo?.map { mapPronunciationInfoToDomain(it) } as MutableList<TargetWordPronunciationInfoEntity>?
        )

    }

    private fun mapExampleInfoToDomain(dto: TargetWordExampleInfoEntity): TargetWordExampleInfoEntity {
        return TargetWordExampleInfoEntity(
            type = dto.type,
            example = dto.example
        )

    }

    private fun mapPronunciationInfoToDomain(dto: TargetWordPronunciationInfoEntity): TargetWordPronunciationInfoEntity {
        return TargetWordPronunciationInfoEntity(
            pronunciation = dto.pronunciation,
            audioUrl = dto.audioUrl
        )
    }

    /**
     * 
     */
}




