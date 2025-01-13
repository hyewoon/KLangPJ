package com.hye.sesac.data.repo

import com.hye.sesac.data.entity.Channel
import com.hye.sesac.data.entity.WordInfo

interface WordRepository {
    suspend fun getWordInfo(inputWord: String) : Channel

}