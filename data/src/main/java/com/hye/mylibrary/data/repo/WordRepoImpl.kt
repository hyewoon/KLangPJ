package com.hye.mylibrary.data.repo



import com.hye.domain.model.WordEntity
import com.hye.domain.repo.WordRepository
import com.hye.domain.result.ApiResult
import com.hye.mylibrary.data.mapper.WordMapper
import com.hye.mylibrary.data.rest.RetrofitRESTService

class WordRepoImpl(private val retrofitRESTService: RetrofitRESTService, private val apiKey: String) :
    WordRepository {
    private val mapper: WordMapper = WordMapper()

    override suspend fun getWordInfo(inputWord: String): ApiResult<List<WordEntity>> {
        return runCatching {
            val response = retrofitRESTService.getWordInfo(apiKey, inputWord) //Channel로 받아옴

            response.item?.let { items ->
                ApiResult.Success(items.map { mapper.mapToDomain(it) }) //Channel로 받아온 걸 다시 WordEntity로 변환
            } ?: ApiResult.Success(emptyList())

        }.getOrElse { e ->
            ApiResult.Failure(e.message ?: "Unknown error")
        }
    }
}




