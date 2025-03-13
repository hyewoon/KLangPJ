package com.hye.mylibrary.data.datasource.repo

import androidx.core.content.ContentProviderCompat.requireContext
import androidx.room.withTransaction
import com.hye.domain.model.TargetWordEntity
import com.hye.domain.model.TargetWordWithAllInfoEntity
import com.hye.domain.repo.StudyRoomRepository
import com.hye.domain.result.RoomDBResult
import com.hye.mylibrary.data.datasource.mapper.ToDomainMapper
import com.hye.mylibrary.data.datasource.mapper.ToRoomDbMapper
import com.hye.mylibrary.data.room.TargetWordRoomDatabase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class StudyRoomRepoImpl(
    private val roomDatabase: TargetWordRoomDatabase,
) : StudyRoomRepository {
    private val targetWordDao = roomDatabase.targetWordDao()
    private val roomMapper: ToRoomDbMapper = ToRoomDbMapper()
    private val domainMapper: ToDomainMapper = ToDomainMapper()


    override suspend fun createStudyRoom(word: List<TargetWordEntity>): RoomDBResult<Unit> {
        return roomDatabase.withTransaction {
            try {
                insertRoomDb(word)
                RoomDBResult.Success(Unit)
            } catch (e: Exception) {
                RoomDBResult.RoomDBError(e)
            }
        }
    }

    override suspend fun readStudyRoom(date: String): RoomDBResult<List<TargetWordWithAllInfoEntity>> =
        runCatching {
            val room = targetWordDao.searchTargetWordByDate(date).map {
                domainMapper.mapToDomain(it)
            }
            RoomDBResult.Success(room)
        }.getOrElse {
            RoomDBResult.RoomDBError(it)

        }


    private suspend fun insertRoomDb(dto: List<TargetWordEntity>) {
        dto.forEach {
            val currentTime: Long = System.currentTimeMillis()
            val currentDate: Date = Date()
            val dataFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
            val today = dataFormat.format(currentDate)
            var targetWord = roomMapper.mapToRoom(it)
            targetWord = targetWord.copy(timeStamp = currentTime, todayString = today)

            targetWordDao.insertLimitedTargetWordExampleInfo(
                targetWord = targetWord,
                exampleInfo = it.exampleInfo?.map { example ->
                    roomMapper.mapToRoomExampleInfo(example, targetWord.targetCode)
                } ?: emptyList(),
                pronunciationInfo = it.pronunciationInfo?.map { pronunciation ->
                    roomMapper.mapToRoomPronunciationInfo(pronunciation, targetWord.targetCode)

                } ?: emptyList()
            )
        }

    }

}