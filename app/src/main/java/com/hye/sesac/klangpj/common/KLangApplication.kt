package com.hye.sesac.klangpj.common

import android.app.Application
import com.google.firebase.FirebaseApp

class KLangApplication: Application() {
    override fun onCreate() {
        super.onCreate()
        appInstance = this
        FirebaseApp.initializeApp(this)
    }
    companion object {
        lateinit var appInstance: KLangApplication
        fun getKLangContext() : KLangApplication = appInstance
    }
}