package com.hye.sesac.data.repo

import com.hye.sesac.data.entity.Channel
import com.hye.sesac.data.rest.RetrofitRESTService

class WordRepoImpl(private val retrofitRESTService: RetrofitRESTService, val apiKey: String) : WordRepository {

    override suspend fun getWordInfo(inputWord: String): Channel {
        return try {
            val response = retrofitRESTService.getWordInfo(apiKey, inputWord)
            response

        } catch (e: Exception) {
            println("에러 ${e.message}")

            throw e
        }
    }
}




