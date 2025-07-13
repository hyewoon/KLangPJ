package com.hye.sesac.klangpj.common

import android.app.Application
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import com.google.firebase.Firebase
import com.google.firebase.initialize
import com.hye.domain.repo.DetailWordRepository
import com.hye.domain.service.WordSyncService
import com.hye.domain.repo.StudyRoomRepository
import com.hye.domain.repo.WordRepository
import com.hye.mylibrary.data.datasource.repo.FireStoreRepoImpl
import com.hye.mylibrary.data.datasource.repo.StudyRoomRepoImpl
import com.hye.mylibrary.data.repo.DetailWordRepoImpl
import com.hye.mylibrary.data.repo.WordRepoImpl
import com.hye.mylibrary.data.rest.RetrofitServiceInstance
import com.hye.mylibrary.data.room.TargetWordRoomDatabase
import com.hye.sesac.klangpj.BuildConfig
import com.hye.mylibrary.data.preferences.PreferenceDataStoreManager

private val Application.dataStore: DataStore<Preferences> by preferencesDataStore(name = "app_preferences")
class KLangApplication : Application() {
    private lateinit var database: TargetWordRoomDatabase
    private lateinit var dataStoreManager: PreferenceDataStoreManager

    override fun onCreate() {
        super.onCreate()
        appInstance = this

        //파이어 베이스 초기화
        Firebase.initialize(this)
        //RoomDB 초기화
        database = TargetWordRoomDatabase.getDatabase(this)
        dataStoreManager = PreferenceDataStoreManager(this)
        initRepository()

    }

    private fun initRepository() {
        val retrofitRESTService = RetrofitServiceInstance.getRetrofitServiceInstance()
        wordRepository = WordRepoImpl(
            retrofitRESTService,
            BuildConfig.API_KEY
        )
        detailWordRepository = DetailWordRepoImpl(
          retrofitRESTService,
            BuildConfig.API_KEY
        )
        firestoreRepository = FireStoreRepoImpl(dataStoreManager
        )
        studyRoomRepository = StudyRoomRepoImpl(database
        )

    }

    companion object {
        lateinit var appInstance: KLangApplication
        fun getKLangContext(): KLangApplication = appInstance

        lateinit var wordRepository: WordRepository
            private set

        lateinit var detailWordRepository: DetailWordRepository
            private set


        lateinit var firestoreRepository: WordSyncService

        lateinit var studyRoomRepository: StudyRoomRepository

        fun getDataStoreManager(): PreferenceDataStoreManager {
            return appInstance.dataStoreManager
        }
    }
}