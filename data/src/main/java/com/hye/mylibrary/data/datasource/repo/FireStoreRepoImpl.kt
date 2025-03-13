package com.hye.mylibrary.data.datasource.repo

import androidx.room.withTransaction
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.hye.domain.model.TargetWordEntity
import com.hye.domain.model.TargetWordExampleInfoEntity
import com.hye.domain.model.TargetWordPronunciationInfoEntity
import com.hye.domain.repo.FireStoreRepository
import com.hye.domain.result.FirebaseResult
import com.hye.mylibrary.data.datasource.dto.TargetWordDto
import com.hye.mylibrary.data.datasource.dto.TargetWordExampleInfoDto
import com.hye.mylibrary.data.datasource.dto.TargetWordPronunciationInfoDto
import com.hye.mylibrary.data.datasource.mapper.ToDomainWordMapper
import com.hye.mylibrary.data.datasource.mapper.ToRoomDbMapper
import com.hye.mylibrary.data.room.TargetWordExampleInfo

import com.hye.mylibrary.data.room.TargetWordRoomDatabase
import kotlinx.coroutines.tasks.await
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

/*
* firestore에서 단어 다운로드
* 조건 1: 빈도수 + 조건 2: 가나다순
* 사용자가 입력한 학습갯수에 따라
* */
class FireStoreRepoImpl() : FireStoreRepository {
    private val db: FirebaseFirestore = Firebase.firestore
    private val domainMapper: ToDomainWordMapper = ToDomainWordMapper()

    override suspend fun getFireStoreWord(count: Long): List<TargetWordEntity> = runCatching {
        val snapshot = db.collection("words1")
            .orderBy("frequency", Query.Direction.DESCENDING)
            .orderBy("korean", Query.Direction.ASCENDING)
            .limit(count)
            .get()
            .await()

        println("Firestore다운 시작 ${snapshot.documents}")


        //1. list로 단어 받아오기
        val dtoList = mutableListOf<TargetWordDto>()
        snapshot.documents.forEach { doc ->
            val dto = TargetWordDto()
            dto.targetCode = doc.getLong("targetCode") ?: 0L
            dto.frequency = doc.getLong("frequency") ?: 0L
            dto.korean = doc.getString("korean") ?: ""
            dto.english = doc.getString("english") ?: ""
            dto.wordGrade = doc.getString("wordGrade") ?: ""
            dto.pos = doc.getString("pos") ?: ""

            dto.exampleInfo = parseExampleInfo(doc)
            dto.pronunciationInfo = parsePronunciationInfo(doc)


            dtoList.add(dto)
            println("dtoList:${dto.exampleInfo}")
            println("dtoList:${dto.pronunciationInfo}")

        }

        dtoList.map { domainMapper.mapToDomain(it) }


    }.getOrElse {
        emptyList()
    }


    private fun parseExampleInfo(doc: DocumentSnapshot): List<TargetWordExampleInfoDto> {
        return try {
            val exampleInfo =
                doc.get("example_info") as? List<Map<String, Any>> ?: return emptyList()
            exampleInfo.map { example ->
                TargetWordExampleInfoDto(
                    type = example["type"] as? String ?: "",
                    example = example["example"] as? String ?: ""
                )
            }
        } catch (e: Exception) {
            println("Error parsing example_info: ${e.message}")
            emptyList()
        }
    }

    private fun parsePronunciationInfo(doc: DocumentSnapshot): List<TargetWordPronunciationInfoDto> {
        return try {
            val pronunciationInfo =
                doc.get("pronunciation_info") as? List<Map<String, Any>> ?: return emptyList()
            pronunciationInfo.map { pronunciation ->
                TargetWordPronunciationInfoDto(
                    pronunciation = pronunciation["pronunciation_info.pronunciation"] as? String
                        ?: "",
                    audioUrl = pronunciation["pronunciation_info.link"] as? String ?: ""
                )
            }
        } catch (e: Exception) {
            println("Error parsing pronunciation_info: ${e.message}")
            emptyList()
        }

    }


}