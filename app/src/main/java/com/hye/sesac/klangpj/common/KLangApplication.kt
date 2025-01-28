package com.hye.sesac.klangpj.common

import android.app.Application
import com.google.firebase.Firebase
import com.google.firebase.initialize
import com.hye.sesac.domain.firestore.repo.FireStoreUploadRepo
import com.hye.sesac.klangpj.BuildConfig
import com.hye.sesac.klangpj.ui.viewmodel.OtherViewModel

class KLangApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        appInstance = this

        //파이어 베이스 초기화
        Firebase.initialize(this)
        //FirebaseApp.initializeApp(this)
        val repository = FireStoreUploadRepo(this, BuildConfig.API_KEY)
        val viewModel = OtherViewModel(repository)
        viewModel.checkAndUploadData()

    }

    companion object {
        lateinit var appInstance: KLangApplication
        fun getKLangContext(): KLangApplication = appInstance
    }
}