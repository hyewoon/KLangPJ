package com.hye.sesac.data.repo

import com.hye.sesac.data.entity.Channel

interface WordRepository {
    suspend fun getWordInfo(inputWord: String) : Channel
    //suspend fun getDetailWordInfo(inputWord: String) : Channel

}