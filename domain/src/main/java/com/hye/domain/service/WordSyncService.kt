package com.hye.domain.service


import com.hye.domain.model.TargetWordEntity

interface WordSyncService {
    suspend fun getFireStoreWord(count: Long) : List<TargetWordEntity>

}