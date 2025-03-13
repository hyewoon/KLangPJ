package com.hye.mylibrary.data.repo

import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.hye.domain.model.FireStoreWord
import com.hye.domain.repo.FireStoreRepository
import com.hye.domain.result.FirebaseResult
import com.hye.mylibrary.data.dto.FireStoreDto
import com.hye.mylibrary.data.mapper.FireStoreMapper
import kotlinx.coroutines.tasks.await

class FireStoreRepoImpl(
) : FireStoreRepository {
    private val db: FirebaseFirestore = Firebase.firestore
    private val mapper: FireStoreMapper = FireStoreMapper()

    companion object FireStoreFactory {
        fun create() = FireStoreRepoImpl()
    }

    override suspend fun getFireStoreWord(count: Long): FirebaseResult<List<FireStoreWord>> {
       return runCatching {
            val dtoList = db.collection("words1")
                .orderBy("english", Query.Direction.ASCENDING)
                .limit(count)
                .get()
                .await()
                .toObjects(FireStoreDto::class.java)

            val domainList = dtoList.map { dto ->
                mapper.mapToDomain(dto)
            }
           println("Success: $domainList")
            FirebaseResult.Success(domainList)
        }.getOrElse { e ->
            println("Error: $e")
            FirebaseResult.Failure(e)
        }
    }


}