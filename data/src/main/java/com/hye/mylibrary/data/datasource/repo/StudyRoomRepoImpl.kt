package com.hye.mylibrary.data.datasource.repo


import android.os.Build
import androidx.annotation.RequiresApi
import com.hye.domain.model.TargetWordEntity
import com.hye.domain.model.TargetWordWithAllInfoEntity
import com.hye.domain.repo.StudyRoomRepository
import com.hye.domain.result.RoomDBResult
import com.hye.mylibrary.data.datasource.mapper.ToDomainMapper
import com.hye.mylibrary.data.datasource.mapper.ToRoomDbMapper
import com.hye.mylibrary.data.room.TargetWordRoomDatabase
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.Date
import java.util.Locale

class StudyRoomRepoImpl(
    private val roomDatabase: TargetWordRoomDatabase,
) : StudyRoomRepository {
    private val targetWordDao = roomDatabase.targetWordDao()
    private val roomMapper: ToRoomDbMapper = ToRoomDbMapper()
    private val domainMapper: ToDomainMapper = ToDomainMapper()


    @RequiresApi(Build.VERSION_CODES.O)
    override suspend fun createStudyRoom(word: List<TargetWordEntity>) = try {
        insertRoomDb(word)
        RoomDBResult.Success(Unit)
    } catch (e: Exception) {
        RoomDBResult.RoomDBError(e)
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

    override suspend fun deleteAllStudyRoom() = try {
        targetWordDao.deleteAll()
        RoomDBResult.Success(Unit)
    } catch (e: Exception) {
        RoomDBResult.RoomDBError(e)
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
                    roomMapper.mapToRoomExampleInfo(example, targetWord.targetCode)
                } ?: emptyList(),
                pronunciationInfo = it.pronunciationInfo.map { pronunciation ->
                    roomMapper.mapToRoomPronunciationInfo(pronunciation, targetWord.targetCode)

                } ?: emptyList()
            )
        }

    }

}