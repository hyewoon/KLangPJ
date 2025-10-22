package com.hye.domain.repo


import com.hye.domain.model.TargetWordEntity

interface FireStoreRepository {
    suspend fun getFireStoreWord(count: Long) : List<TargetWordEntity>

}