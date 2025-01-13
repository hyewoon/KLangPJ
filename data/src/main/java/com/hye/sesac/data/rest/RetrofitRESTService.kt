package com.hye.sesac.data.rest

import com.hye.sesac.data.entity.Channel
import com.hye.sesac.data.entity.WordInfo
import retrofit2.http.GET
import retrofit2.http.Query


const val BASE_URL = "https://krdict.korean.go.kr/"

interface RetrofitRESTService {

    //requestQuery
    @GET("api/search")
    suspend fun getWordInfo(
        //필수 : 인증키, 쿼리(입력값)
        @Query("key") key: String,
        @Query("q") q: String,
        //정렬방식
        @Query("sort") sort: String = "popular",
        //검색 대상 : 검색어
        @Query("part") part: String = "word",
        //다국어 번역 여부
        @Query("translated") translated: String = "y",
        //번역어 : 영어
        @Query("trans_lang") transLang: String = "1",
        @Query("advanced") advanced: String = "y",
        //일치하는 값만
        @Query("method") method: String = "exact",
        @Query("type1") type1 :List<String> = listOf(),
        //한국어 레벨
        @Query("level") level: List<String> = listOf(),
        @Query("pos") pos : List<Int> = listOf(),
        @Query("multimedia") multimedia : List<Int> = listOf(5),

        ) : Channel


}