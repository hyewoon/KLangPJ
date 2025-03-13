package com.hye.mylibrary.data.repo

import com.hye.domain.model.TargetWordWithAllInfoEntity
import com.hye.domain.repo.StudyRoomRepository
import com.hye.domain.result.RoomDBResult
import com.hye.mylibrary.data.datasource.mapper.ToRoomDbMapper
import com.hye.mylibrary.data.room.TargetWordRoomDatabase

class StudyRoomRepoImpl(private val roomDatabase: TargetWordRoomDatabase,) : StudyRoomRepository {
    private val roomMapper: ToRoomDbMapper = ToRoomDbMapper()
    private val targetWordDao = roomDatabase.targetWordDao()

    override suspend fun createStudyRoom(data: TargetWordWithAllInfoEntity) {

    }

    override suspend fun readStudyRoom(date: String): RoomDBResult<List<TargetWordWithAllInfoEntity>> {
        TODO("Not yet implemented")
    }
}