package com.hye.mylibrary.data.rest

import com.tickaroo.tikxml.TikXml
import com.tickaroo.tikxml.retrofit.TikXmlConverterFactory
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit

const val BASE_URL = "https://krdict.korean.go.kr/"

class RetrofitServiceInstance {

    companion object {

        private val loggingInterceptor = HttpLoggingInterceptor().apply {
            //요청, 응답 본문 내용 까지 포함
            level = HttpLoggingInterceptor.Level.BODY
            //요청, 응답 URL, 메소드, 응답 코드, 응답 시간만
            //level = HttpLoggingInterceptor.Level.BASIC

        }

        /**
         * okHttp 클라이언트 구성
         * 여기서 로깅 인터셉트 추가하여 네트워크 요청 시 로그가 찍히도록 설정
         */
        private val client = OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)
            .build()

        private lateinit var retrofitService: RetrofitRESTService
        private val parser = TikXml.Builder().exceptionOnUnreadXml(false).build()
        fun getRetrofitServiceInstance(): RetrofitRESTService {
            if (!this::retrofitService.isInitialized) {
                val retrofit = Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .client(client) //로깅 기능 추가된 okhttp를 retrofit에 추가
                    .addConverterFactory(TikXmlConverterFactory.create(parser))
                    .build()
                retrofitService = retrofit.create(RetrofitRESTService::class.java)


            }
            return retrofitService

        }
    }
}