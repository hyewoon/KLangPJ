package com.hye.domain.repo


import com.hye.domain.model.DetailWordEntity
import com.hye.domain.result.ApiResult

interface DetailWordRepository {
    suspend fun getDetailWordInfo(targetCode: String): ApiResult<List<DetailWordEntity>>

}