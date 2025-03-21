package com.hye.mylibrary.data.datasource.repo


import androidx.core.content.ContentProviderCompat.requireContext
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.QuerySnapshot
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.hye.domain.model.TargetWordEntity
import com.hye.domain.repo.FireStoreRepository
import com.hye.mylibrary.data.datasource.dto.TargetWordDto
import com.hye.mylibrary.data.datasource.dto.TargetWordExampleInfoDto
import com.hye.mylibrary.data.datasource.dto.TargetWordPronunciationInfoDto
import com.hye.mylibrary.data.datasource.mapper.ToDomainWordMapper
import com.hye.sesac.klangpj.data.preferences.PreferenceDataStoreManager
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.tasks.await

/*
* firestore에서 단어 다운로드
* 조건 1: 빈도수 + 조건 2: 가나다순
* 사용자가 입력한 학습갯수에 따라
* */
class FireStoreRepoImpl(private val preferenceDataStoreManager: PreferenceDataStoreManager) :
    FireStoreRepository {
    private val db: FirebaseFirestore = Firebase.firestore
    private val domainMapper: ToDomainWordMapper = ToDomainWordMapper()

    override suspend fun getFireStoreWord(count: Long): List<TargetWordEntity> = runCatching {

        val lastDocId = preferenceDataStoreManager.documentId.first()

        val snapshot = getFireStoreData(lastDocId, count)

        if (snapshot.documents.isNotEmpty()) {
            val lastDocumentId = snapshot.documents.last().id
            preferenceDataStoreManager.saveDocumentId(lastDocumentId)
        }

        val dtoList = snapshot.documents.map { mapToTargetWordDto(it) }
        dtoList.map { domainMapper.mapToDomain(it) }
    }.getOrElse {
        emptyList()
    }

    private suspend fun getFireStoreData(lastDocId: String, count: Long) =
        if (lastDocId.isEmpty()) { //1. 맨 처음 다운로드 dataStore에 값이 없음
            db.collection("words1")
                .orderBy("frequency", Query.Direction.DESCENDING)
                .orderBy("korean", Query.Direction.ASCENDING)
                .limit(count)

        } else {
            //2 값이 존재하면 문서참조얻기
            val lastDocRef = db.collection("words1").document(lastDocId)
            val lastDoc = lastDocRef.get().await()

            db.collection("words1")
                .orderBy("frequency", Query.Direction.DESCENDING)
                .orderBy("korean", Query.Direction.ASCENDING)
                .startAfter(lastDoc) // 마지막 문서 다음부터 시작
                .limit(count)
        }
            .get()
            .await()
}

private fun mapToTargetWordDto(doc: DocumentSnapshot) = TargetWordDto().apply {
    targetCode = doc.getLong("targetCode") ?: 0L
    frequency = doc.getLong("frequency") ?: 0L
    korean = doc.getString("korean") ?: ""
    english = doc.getString("english") ?: ""
    wordGrade = doc.getString("wordGrade") ?: ""
    pos = doc.getString("pos") ?: ""

    exampleInfo = parseExampleInfo(doc)
    pronunciationInfo = parsePronunciationInfo(doc)
}.also {
    println("exampleInfo: ${it.exampleInfo}")
    println("pronunciationInfo: ${it.pronunciationInfo}")
}



private fun parseExampleInfo(doc: DocumentSnapshot) = runCatching {
    val exampleInfo =
        doc.get("example_info") as? List<Map<String, Any>> ?: emptyList<Map<String, Any>>()
    exampleInfo.map { example ->
        TargetWordExampleInfoDto(
            type = example["type"] as? String ?: "",
            example = example["example"] as? String ?: ""
        )
    }
}.onFailure {
    println("Error parsing example_info: ${it.message}")

}.getOrDefault(emptyList())


private fun parsePronunciationInfo(doc: DocumentSnapshot) = runCatching {
    val pronunciationInfo =
        doc.get("pronunciation_info") as? List<Map<String, Any>> ?: emptyList<Map<String, Any>>()
    pronunciationInfo.map { pronunciation ->
        TargetWordPronunciationInfoDto(
            pronunciation = pronunciation["pronunciation_info.pronunciation"] as? String
                ?: "",
            audioUrl = pronunciation["pronunciation_info.link"] as? String ?: ""
        )
    }
}.onFailure {
    println("Error parsing pronunciation_info: ${it.message}")

}.getOrDefault(emptyList())




