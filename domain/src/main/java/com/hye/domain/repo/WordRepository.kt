package com.hye.domain.repo




import com.hye.domain.model.WordEntity
import com.hye.domain.result.ApiResult

interface WordRepository {
    suspend fun getWordInfo(inputWord: String) : ApiResult<List<WordEntity>>

}