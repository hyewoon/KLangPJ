package com.hye.mylibrary.data.rest
import com.hye.mylibrary.data.dto.Channel
import com.hye.mylibrary.data.dto.DetailChannel
import retrofit2.http.GET
import retrofit2.http.Query




interface RetrofitRESTService {

    //단어 검색 Channel객체는 api로 받아오는 최상위 클래스
    @GET("/api/search")
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
        //소리의 경우, 5 입력
        @Query("multimedia")multimedia: List<Int> = listOf()
        ) : Channel

    //상세 단어 정보
    @GET("/api/view")
    suspend fun getDetailWordInfo(
        @Query("key") key: String,
        @Query("q") targetCode: String ,
        @Query("method") method: String ="target_code",
        @Query("translated") translated: String = "y",
        @Query("trans_lang") transLang: String = "1"
    ) : DetailChannel

    @GET("/api/search")
    suspend fun getWordCount(
        @Query("key") key: String,           // API 키 (필수)
        @Query("start") start: Int = 1,      // 검색 시작 위치 (1~1000)
        @Query("num") num: Int = 100,        // 검색 결과 수 (10~100)
    ): Channel

}