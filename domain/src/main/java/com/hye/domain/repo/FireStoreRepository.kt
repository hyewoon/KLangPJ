package com.hye.mylibrary.data.datasource.repo


import com.hye.domain.model.TargetWordEntity
import com.hye.domain.result.FirebaseResult

interface FireStoreRepository {
    suspend fun getFireStoreWord(count: Long) : FirebaseResult<List<TargetWordEntity>>

}