package com.hye.mylibrary.data.datasource.mapper

import com.hye.domain.model.TargetWordEntity
import com.hye.domain.model.TargetWordExampleInfoEntity
import com.hye.domain.model.TargetWordPronunciationInfoEntity
import com.hye.mylibrary.data.datasource.dto.TargetWordDto
import com.hye.mylibrary.data.datasource.dto.TargetWordExampleInfoDto
import com.hye.mylibrary.data.datasource.dto.TargetWordPronunciationInfoDto
import com.hye.mylibrary.data.room.TargetWord
import com.hye.mylibrary.data.room.TargetWordExampleInfo
import com.hye.mylibrary.data.room.TargetWordPronunciationInfo

/**
 * firebase에서 받은 데이터를 roomDB에 저장하기 위한 mapper
 */
class ToRoomDbMapper {
    fun mapToRoom(dto: TargetWordEntity): TargetWord {
        return TargetWord(
            documentId = dto.documentId,
            targetCode = dto.targetCode,
            frequency = dto.frequency,
            korean = dto.korean,
            english = dto.english,
            pos = dto.pos,
            wordGrade = dto.wordGrade?: "등급 없음"
        )
    }

    fun mapToRoomExampleInfo(
        dto: TargetWordExampleInfoEntity,
        documentId: String,
    )= TargetWordExampleInfo(
            documentId = documentId,
            type = dto.type?:"",
            example = dto.example?: "예시가 존재 하지 않음"
        )

    fun mapToRoomPronunciationInfo(
        dto: TargetWordPronunciationInfoEntity,
        documentId: String,
    )= TargetWordPronunciationInfo(
            documentId = documentId,
            pronunciation = dto.pronunciation?: "",
            audioUrl = dto.audioUrl?:"음성 파일 존재 하지 않음"
        )

}