package com.hye.sesac.data.repo

import com.hye.sesac.data.entity.Channel
import com.hye.sesac.data.rest.RetrofitRESTService

class WordRepoImp(private val retrofitRESTService: RetrofitRESTService, private val apiKey: String) : WordRepository {

    override suspend fun getWordInfo(inputWord: String): Channel  = retrofitRESTService.getWordInfo(apiKey,inputWord)



}