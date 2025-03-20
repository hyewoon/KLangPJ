package com.hye.domain.usecase

import com.hye.domain.model.TargetWordWithAllInfoEntity
import com.hye.domain.repo.FireStoreRepository
import com.hye.domain.repo.StudyRoomRepository
import com.hye.domain.result.RoomDBResult
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Date
import java.util.Locale
import kotlin.math.E

/**
 * 오늘 날짜 비교해서
 * -> 오늘 날짜에 해당하는 데이터가 존재하는지 확인
 * 데이터가 존재하지 않음(isEmpty())-> firestore에서 다운받기
 */
class LoadTodayStudyWord(
    private val firestoreRepository: FireStoreRepository,
    private val studyRoomRepository: StudyRoomRepository,
) {
    suspend operator fun invoke(targetWord: Int): RoomDBResult<List<TargetWordWithAllInfoEntity>> {
        //val today = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())
        val today =
            LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd", Locale.getDefault()))

        println("오늘날짜: $today ")
        val count = targetWord.toLong()
        return when (val roomData = studyRoomRepository.readAllStudyRoom()) {
            is RoomDBResult.Success -> {
                if (roomData.data.isEmpty()) { //room에 저장된 정보가 없음
                    try {
                        fetchAndSaveData(count, today)
                        studyRoomRepository.readStudyRoom(today)
                    } catch (e: Exception) {
                        RoomDBResult.RoomDBError(e)
                    }
                } else { //room에 저장된 정보가 있음
                    val todayData = roomData.data.filter { it.todayString == today }
                    println("todayData = $todayData")
                    if (todayData.isEmpty()) { // 저장된 정보는 있지만 오늘 정보 없음
                        try {
                            studyRoomRepository.deleteAllStudyRoom() //기존 정보 삭제
                            fetchAndSaveData(count, today) //오늘 정보 다운 + 저장
                            studyRoomRepository.readStudyRoom(today)
                        } catch (e: Exception) {
                            RoomDBResult.RoomDBError(e)
                        }
                    } else { //저장된 정보+ 오늘에 해당함
                        studyRoomRepository.readStudyRoom(today)

                    }
                }
            }

            is RoomDBResult.RoomDBError -> roomData
            else -> RoomDBResult.RoomDBError(Exception("Unknown error"))
        }

        /*   return when (val roomData = studyRoomRepository.readStudyRoom(today)) {
               is RoomDBResult.Success -> { //쿼리를 잘 수행함(Success)
                   if (roomData.data.isEmpty()) { //오늘날짜에 해당하는 데이터베이스 존재하는지
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
           }*/
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






