package com.hye.domain.usecase

import com.hye.domain.model.TargetWordWithAllInfoEntity
import com.hye.domain.repo.FireStoreRepository
import com.hye.domain.repo.StudyRoomRepository
import com.hye.domain.result.RoomDBResult
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import kotlin.math.E

class LoadTodayStudyWord(
    private val firestoreRepository: FireStoreRepository,
    private val studyRoomRepository: StudyRoomRepository,
) {
    suspend operator fun invoke(targetWord: Int): RoomDBResult<List<TargetWordWithAllInfoEntity>> {
        val today = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())

        return when (val roomData = studyRoomRepository.readStudyRoom(today)) {
            is RoomDBResult.Success -> {
                if (roomData.data.isEmpty()) {
                    try {
                        val count = targetWord.toLong()
                        fetchAndSaveData(count, today)
                        studyRoomRepository.readStudyRoom(today)
                    } catch (e: Exception) {
                        RoomDBResult.RoomDBError(e)
                    }

                } else {
                    roomData
                }
            }

            is RoomDBResult.RoomDBError -> {
                roomData
            }

            else -> {
                RoomDBResult.RoomDBError(Exception("Unknown error"))
            }

        }

    }

    private suspend fun fetchAndSaveData(
        count: Long,
        today: String,
    ): RoomDBResult<List<TargetWordWithAllInfoEntity>> {
        return try {
            val firestoreData = firestoreRepository.getFireStoreWord(count)
            println("firestore다운 : $firestoreData")
            val saveRoom = studyRoomRepository.createStudyRoom(firestoreData)

            when (saveRoom) {
                is RoomDBResult.Success -> {
                    studyRoomRepository.readStudyRoom(today)
                }

                is RoomDBResult.RoomDBError -> {
                    RoomDBResult.RoomDBError(Exception("RoomDB Error"))
                }

                else -> {
                    RoomDBResult.RoomDBError(Exception("Unknown error"))
                }
            }
        } catch (e: Exception) {
            RoomDBResult.RoomDBError(e)
        }

    }
}






