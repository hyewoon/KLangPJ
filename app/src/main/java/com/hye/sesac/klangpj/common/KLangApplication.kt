package com.hye.sesac.klangpj.common

import android.app.Application

class KLangApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        appInstance = this


    }

    companion object {
        lateinit var appInstance: KLangApplication
        fun getKLangContext(): KLangApplication = appInstance
    }
}