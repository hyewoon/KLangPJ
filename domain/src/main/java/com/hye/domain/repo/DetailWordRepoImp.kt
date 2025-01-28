package com.hye.sesac.data.repo

import com.hye.sesac.data.entity.DetailChannel
import com.hye.sesac.data.rest.RetrofitRESTService

class DetailWordRepoImp(private val retrofitRESTService: RetrofitRESTService, private val apiKey: String){

     suspend fun getDetailWordInfo(targetCode: String) : DetailChannel {
        return try{
            retrofitRESTService.getDetailWordInfo(
                apiKey, targetCode
            )

        }catch (e: Exception){
            throw e
        }
    }
}