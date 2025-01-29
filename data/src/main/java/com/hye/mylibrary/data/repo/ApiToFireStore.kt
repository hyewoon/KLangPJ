package com.hye.sesac.domain.firestore.repo

import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.SetOptions
import com.google.firebase.firestore.WriteBatch
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.hye.sesac.data.rest.RetrofitRESTService
import com.hye.sesac.data.rest.RetrofitServiceInstance
import kotlinx.coroutines.processNextEventInCurrentThread
import kotlinx.coroutines.tasks.await

class ApiToFireStore(
    private val retrofitService: RetrofitRESTService = RetrofitServiceInstance.getRetrofitServiceInstance(),
    private val apiKey: String,
) {
    private val db = Firebase.firestore

    companion object {
        private const val FIREBASE_COLLECTION_NAME = "words1"
        private const val BATCH_SIZE = 100
    }

    /**
     * api 요청 해서 데이터 가지고 오기
     *
     * 1. firebase읽어오기 get().await()
     * api 요청 (/api/search)
     * word1의 korean == api word -> words1의 targetCode, wordGrade,definition
     * api 요청 (/api/view)_
     * word1의 targetCode == api targetCode -> words1의 pronunciation, example 가져오기
     * pronunciation, example 가져오기
     * field targetCode = api.targetCode
     * 이때 pronunciation이 == null 값이면?: "음성파일이 없습니다"
     *
     */
    suspend fun uploadDataFromApiToFireStore() {
        // changeType()
        //기본 정보 api 호출
        // loadBasicWordInfo()
        //상세 정보 api 호출
        // loadDetailWordInfo()
        //deleteFields()
        //loadDetailInfo()
        loadExampleInfo()

    }

 @Suppress("UNCHECKED_CAST")
    private suspend fun loadExampleInfo() {
        val docRef = db.collection(FIREBASE_COLLECTION_NAME).get().await()
        var batchCount = 0
        var batch = db.batch()

        docRef.documents.chunked(BATCH_SIZE).forEach { batchDocs ->
            batchDocs.forEach { document ->
                val target = document.getLong("targetCode")
                if (target != null) {

                    try {
                        val apiResponse =
                            retrofitService.getDetailWordInfo(apiKey, target.toString()).items
                                ?: return@forEach
                        println(apiResponse)

                        val responseList = apiResponse.flatMap { response ->
                            response.wordInfo?.senseInfo?.flatMap { senseInfo ->
                                senseInfo.exampleInfo?.map {

                                    mapOf(
                                        "example" to it.example,
                                        "type" to it.type,

                                        )
                                } ?: emptyList()

                            } ?: emptyList()

                        }
                        // 각 pronunciationMap을 batch에 추가

                        val currentData = document.data ?: emptyMap()
                        val newData = currentData.toMutableMap().apply {

                            val existingPronunciations =
                                (get("example_info") as? List<Map<String, Any>>)
                                    ?: emptyList()
                            put("example_info", existingPronunciations + responseList)

                        }

                        batch.set(
                            document.reference,
                            newData
                        )
                        batchCount++

                        // batch 크기가 100이 되면 커밋
                        if (batchCount >= BATCH_SIZE) {
                            batch.commit().await()
                            println("Batch committed: $BATCH_SIZE updates processed")
                            batch = db.batch()
                            batchCount = 0
                        }
                    } catch (e: Exception) {
                        println("Error processing document ${document.id}: ${e.message}")
                        // Continue with next document instead of failing entire batch
                        return@forEach

                    }

                }
            }
        }

        // 남은 batch 처리
        if (batchCount > 0) {
            batch.commit().await()
            println("Final batch committed: $batchCount updates processed")
        }
    }

    @Suppress("UNCHECKED_CAST")
        private suspend fun loadDetailInfo() {
            val docRef = db.collection(FIREBASE_COLLECTION_NAME).get().await()
            var batchCount = 0
            var batch = db.batch()

            docRef.documents.chunked(BATCH_SIZE).forEach { batchDocs ->
                batchDocs.forEach { document ->
                    val target = document.getLong("targetCode")
                    if (target != null) {

                        try {
                            val apiResponse =
                                retrofitService.getDetailWordInfo(apiKey, target.toString()).items
                                    ?: return@forEach
                            println(apiResponse)

                            val responseList = apiResponse.flatMap { response ->
                                response.wordInfo?.pronunciationInfo?.map { pronunciationInfo ->
                                    mapOf(
                                        "pronunciation_info.pronunciation" to pronunciationInfo.pronunciation,
                                        "pronunciation_info.link" to pronunciationInfo.link,

                                        )
                                } ?: emptyList()

                            }
                            // 각 pronunciationMap을 batch에 추가

                            val currentData = document.data ?: emptyMap()
                            val newData = currentData.toMutableMap().apply {

                                val existingPronunciations =
                                    (get("pronunciation_info") as? List<Map<String, Any>>)
                                        ?: emptyList()
                                put("pronunciation_info", existingPronunciations + responseList)

                            }

                            batch.set(
                                document.reference,
                                newData
                            )
                            batchCount++

                            // batch 크기가 100이 되면 커밋
                            if (batchCount >= BATCH_SIZE) {
                                batch.commit().await()
                                println("Batch committed: $BATCH_SIZE updates processed")
                                batch = db.batch()
                                batchCount = 0
                            }
                        } catch (e: Exception) {
                            println("Error processing document ${document.id}: ${e.message}")
                            // Continue with next document instead of failing entire batch
                            return@forEach

                        }

                    }
                }
            }

            // 남은 batch 처리
            if (batchCount > 0) {
                batch.commit().await()
                println("Final batch committed: $batchCount updates processed")
            }
        }

        @Suppress("UNCHECKED_CAST")
        suspend fun loadBasicWordInfo() {
            try {
                val docRef = db.collection(FIREBASE_COLLECTION_NAME).get().await()
                var totalProcessed = 0
                var noMatchCount = 0

                //ducuments를 100개씩 묶어서
                docRef.documents.chunked(BATCH_SIZE).forEach { batchDocs -> //100개 묶음 documents
                    var currentBatch: WriteBatch = db.batch()
                    var batchCount = 0

                    batchDocs.forEach {
                        try {
                            val korean =
                                it.getString("korean") ?: throw Exception("korean field is null")
                            //api 요청
                            val apiResult = retrofitService.getWordInfo(apiKey, korean).item
                                ?: throw Exception("Api is null")

                            val matching = apiResult?.firstOrNull { apiWord ->
                                apiWord.word == korean
                            } ?: throw Exception("no matching word")

                                currentBatch.update(
                                    it.reference, mapOf(
                                        "targetCode" to matching.targetCode,
                                        "wordGrade" to matching.wordGrade
                                    )
                                )
                                batchCount++
                                println("배치에 안 더해짐 : ${it.id}")


                                if (batchCount >= 100) {
                                    currentBatch.commit().await()
                                    println("커밋성공${batchCount}")
                                    totalProcessed += batchCount
                                    batchCount = 0
                                    currentBatch = db.batch()
                                }

                        } catch (e: Exception) {
                            println()
                        }

                    }

                    if (batchCount > 0) {
                        try {
                            currentBatch.commit().await()
                            println("커밋성공${batchCount}")
                            totalProcessed += batchCount
                            batchCount = 0
                            currentBatch = db.batch()

                        } catch (e: Exception) {
                            println("에러 발생 ${e.message}")

                        }


                    }

                }

            } catch (e: Exception) {
                println()
            }
        }

        /* private suspend fun loadDetailWordInfo() {
         try {


             val docRef = db.collection(FIREBASE_COLLECTION_NAME).get().await()
             val batch = db.batch()
             docRef.documents.chunked(BATCH_SIZE).forEach { batchDocs ->

                 batchDocs.forEach {
                     try {

                         val currentExample = it.getString("example")

                         if (it.contains("example")) {
                             val updateExample = listOf(
                                 mapOf(
                                     "type" to "",
                                     "example" to currentExample
                                 )
                             )
                             batch.update(it.reference, "example", updateExample)
                         }

                         val targetCode = it.getString("targetCode").toString()
                         val apiResponse =
                             retrofitService.getDetailWordInfo(apiKey, targetCode).items
                         println("apiResponse : $apiResponse")



                         if (apiResponse != null) {
                             val newExample =
                                 apiResponse.firstOrNull()?.wordInfo?.senseInfo?.firstOrNull()?.exampleInfo?.firstOrNull()

                             if (newExample != null) {
                                 batch.update(
                                     it.reference,
                                     mapOf(
                                         "pronunciation" to apiResponse.firstOrNull()?.wordInfo?.pronunciationInfo?.firstOrNull()?.link,
                                         "example" to
                                                 listOf(
                                                     mapOf(
                                                         "type" to newExample.type,
                                                         "example" to newExample.example
                                                     )
                                                 )


                                     )


                                 )
                             }

                         }


                     } catch (e: Exception) {
                         println("Error processing document ${it.id}: ${e.message}")
                     }


                 }
                 try {
                     batch.commit().await()
                     println("Batch update successful")
                 } catch (e: Exception) {
                     println("Error committing batch: ${e.message}")

                 }
             }

         } catch (e: Exception) {

             println("Error in loadDetailWordInfo: ${e.message}")
         }
        }


        */


        private suspend fun deleteFields() {
            val docRef = db.collection(FIREBASE_COLLECTION_NAME).get().await()
            val batch = db.batch()


            docRef.documents.chunked(BATCH_SIZE).forEach {
                it.forEach { document ->
                    val updates = hashMapOf(
                        "pronunciation" to FieldValue.delete(),
                        "example" to FieldValue.delete()
                    )
                    batch.update(document.reference, updates as Map<String, Any>)
                }


            }
            batch.commit().await()


        }


    }