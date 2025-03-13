package com.hye.mylibrary.data.datasource.mapper

import com.hye.domain.model.TargetWordEntity
import com.hye.domain.model.TargetWordExampleInfoEntity
import com.hye.domain.model.TargetWordPronunciationInfoEntity
import com.hye.mylibrary.data.datasource.dto.TargetWordDto
import com.hye.mylibrary.data.datasource.dto.TargetWordExampleInfoDto
import com.hye.mylibrary.data.datasource.dto.TargetWordPronunciationInfoDto
import com.hye.mylibrary.data.room.TargetWordExampleInfo

/**
 * firestore에서 받아온 정보를 domain으로 변환하는 mapper
 *
 */
class ToDomainWordMapper {
    /**
     * to domain
     */
    fun mapToDomain(dto: TargetWordDto): TargetWordEntity {
        return TargetWordEntity(
            targetCode = dto.targetCode,
            frequency = dto.frequency ?: 0L,
            korean = dto.korean,
            english = dto.english,
            pos = dto.pos,
            wordGrade = dto.wordGrade ?: "등급 없음",
            exampleInfo = (dto.getFilteredExamples("구", 3)
                    + dto.getFilteredExamples("문장", 3))
                .map {
                    TargetWordExampleInfoEntity(
                        type = it.type,
                        example = it.example
                    )
                }?.toMutableList(),
            pronunciationInfo = dto.pronunciationInfo?.map {
                TargetWordPronunciationInfoEntity(
                    pronunciation = it.pronunciation,
                    audioUrl = it.audioUrl
                )

            }?.toMutableList()
        )

    }

    private fun mapExampleInfoToDomain(dto: TargetWordExampleInfoDto): TargetWordExampleInfoEntity {
        return TargetWordExampleInfoEntity(
            type = dto.type,
            example = dto.example
        )

    }

    private fun mapPronunciationInfoToDomain(dto: TargetWordPronunciationInfoDto): TargetWordPronunciationInfoEntity {
        return TargetWordPronunciationInfoEntity(
            pronunciation = dto.pronunciation,
            audioUrl = dto.audioUrl
        )
    }

}




