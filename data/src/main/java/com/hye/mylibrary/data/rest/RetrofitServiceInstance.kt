package com.hye.sesac.data.rest

import com.hye.domain.rest.BASE_URL
import com.hye.domain.rest.RetrofitRESTService
import com.tickaroo.tikxml.TikXml
import com.tickaroo.tikxml.retrofit.TikXmlConverterFactory
import retrofit2.Retrofit


class RetrofitServiceInstance {

    companion object {


        private lateinit var retrofitService: RetrofitRESTService
        private val parser = TikXml.Builder().exceptionOnUnreadXml(false).build()
        fun getRetrofitServiceInstance(): RetrofitRESTService {
            if (!this::retrofitService.isInitialized) {
                val retrofit = Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(TikXmlConverterFactory.create(parser))
                    .build()
                retrofitService = retrofit.create(RetrofitRESTService::class.java)


            }
            return retrofitService

        }
    }
}