package com.hye.domain.repo


import com.hye.domain.model.TargetWordEntity
import com.hye.domain.result.FirebaseResult

interface FireStoreRepository {
    suspend fun getFireStoreWord(count: Long) : List<TargetWordEntity>

}