package com.hye.mylibrary.data.datasource.repo


import android.os.Build
import androidx.annotation.RequiresApi
import com.hye.domain.model.TargetWordEntity
import com.hye.domain.repo.StudyRoomRepository
import com.hye.domain.result.RoomDBResult
import com.hye.mylibrary.data.datasource.mapper.ToDomainMapper
import com.hye.mylibrary.data.datasource.mapper.ToRoomDbMapper
import com.hye.mylibrary.data.room.TargetWordRoomDatabase
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Locale

class StudyRoomRepoImpl(
    private val roomDatabase: TargetWordRoomDatabase,
) : StudyRoomRepository {
    private val targetWordDao = roomDatabase.targetWordDao()
    private val roomMapper: ToRoomDbMapper = ToRoomDbMapper()
    private val domainMapper: ToDomainMapper = ToDomainMapper()


    @RequiresApi(Build.VERSION_CODES.O)
    override suspend fun createStudyRoom(word: List<TargetWordEntity>) = runCatching {
        insertRoomDb(word)
        RoomDBResult.Success(Unit)
    }.getOrElse {
        RoomDBResult.RoomDBError(it)
    }

    override suspend fun readStudyRoom(date: String) = runCatching {
        val room = targetWordDao.searchTargetWordByDate(date).map {
            domainMapper.mapToDomain(it)
        }
        RoomDBResult.Success(room)
    }.getOrElse {
        RoomDBResult.RoomDBError(it)
    }

    override suspend fun readAllStudyRoom() = runCatching {
        val room = targetWordDao.getAllTargetWords().map {
            domainMapper.mapToDomain(it)
        }
        RoomDBResult.Success(room)
    }.getOrElse {
        RoomDBResult.RoomDBError(it)
    }

    override suspend fun deleteAllStudyRoom() =runCatching {
        targetWordDao.deleteAll()
        RoomDBResult.Success(Unit)
    }.getOrElse {
        RoomDBResult.RoomDBError(it)
    }


    @RequiresApi(Build.VERSION_CODES.O)
    private suspend fun insertRoomDb(dto: List<TargetWordEntity>) {
        dto.forEach {
            val currentTime: Long = System.currentTimeMillis()
            val currentDate = LocalDate.now()
            val dataFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd", Locale.getDefault())
            val today = currentDate.format(dataFormat)

            var targetWord = roomMapper.mapToRoom(it)
            targetWord = targetWord.copy(timeStamp = currentTime, todayString = today)

            targetWordDao.insertLimitedTargetWordExampleInfo(
                targetWord = targetWord,
                exampleInfo = it.exampleInfo.map { example ->
                    roomMapper.mapToRoomExampleInfo(example, targetWord.documentId)
                } ?: emptyList(),
                pronunciationInfo = it.pronunciationInfo.map { pronunciation ->
                    roomMapper.mapToRoomPronunciationInfo(pronunciation, targetWord.documentId)

                } ?: emptyList()
            )
        }

    }

}