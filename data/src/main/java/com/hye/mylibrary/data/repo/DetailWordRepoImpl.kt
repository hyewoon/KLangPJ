package com.hye.mylibrary.data.repo

import com.hye.domain.model.DetailWordEntity
import com.hye.domain.repo.DetailWordRepository
import com.hye.domain.result.ApiResult
import com.hye.mylibrary.data.mapper.DetailWordInfoMapper
import com.hye.mylibrary.data.rest.RetrofitRESTService

class DetailWordRepoImpl(
    private val retrofitRESTService: RetrofitRESTService,
    private val apiKey: String,
) :
    DetailWordRepository {
    private val mapper: DetailWordInfoMapper = DetailWordInfoMapper()

    override suspend fun getDetailWordInfo(targetCode: String) = runCatching {
            val response = retrofitRESTService.getDetailWordInfo(apiKey, targetCode)
            response.items?.let{ items->
                ApiResult.Success(items.map { mapper.mapToDomain(it) })
            }?: ApiResult.Success(emptyList())
        }.getOrElse { e->
            ApiResult.Failure(e.message ?: "Unknown error")
        }

}

