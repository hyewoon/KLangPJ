package com.hye.domain.repo

import com.hye.sesac.data.entity.DetailChannel
import com.hye.sesac.data.rest.RetrofitRESTService

class DetailWordRepoImpl(private val retrofitRESTService: RetrofitRESTService, private val apiKey: String):
    DetailWordRepository {

    override suspend fun getDetailWordInfo(targetCode: String): DetailChannel {
        return try {
            retrofitRESTService.getDetailWordInfo(
                apiKey, targetCode
            )

        } catch (e: Exception) {
            throw e
        }
    }
}

