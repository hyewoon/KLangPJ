package com.hye.sesac.klangpj.common

import android.app.Application
import com.google.firebase.Firebase
import com.google.firebase.initialize
import com.hye.sesac.klangpj.ui.dicontainer.AppContainer

//private val Application.dataStore: DataStore<Preferences> by preferencesDataStore(name = "app_preferences")

class KLangApplication : Application() {

    lateinit var appContainer: AppContainer

    override fun onCreate() {
        super.onCreate()
        appInstance = this

        //파이어 베이스 초기화
        Firebase.initialize(this)
       //앱 컨테이너 초기화
         appContainer = AppContainer(this)
    }

    companion object {
        private lateinit var appInstance: KLangApplication
        fun getKLangContext(): KLangApplication = appInstance

    }
}