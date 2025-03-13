package com.hye.domain.repo

import com.hye.domain.model.TargetWordEntity
import com.hye.domain.model.TargetWordWithAllInfoEntity
import com.hye.domain.result.RoomDBResult


interface StudyRoomRepository {
    suspend fun createStudyRoom(word: List<TargetWordEntity>) : RoomDBResult<Unit>
    suspend fun readStudyRoom(date: String):RoomDBResult<List<TargetWordWithAllInfoEntity>>
}