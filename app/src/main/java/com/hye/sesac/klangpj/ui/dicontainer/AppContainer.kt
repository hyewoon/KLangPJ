package com.hye.sesac.klangpj.ui.dicontainer

import android.content.Context
import com.hye.domain.repo.DetailWordRepository
import com.hye.domain.repo.FireStoreRepository
import com.hye.domain.repo.StudyRoomRepository
import com.hye.domain.repo.WordRepository
import com.hye.domain.usecase.LoadTodayStudyWord
import com.hye.mylibrary.data.datasource.repo.FireStoreRepositoryImpl
import com.hye.mylibrary.data.datasource.repo.StudyRoomRepoImpl
import com.hye.mylibrary.data.preferences.PreferenceDataStoreManager
import com.hye.mylibrary.data.repo.DetailWordRepoImpl
import com.hye.mylibrary.data.repo.WordRepoImpl
import com.hye.mylibrary.data.rest.RetrofitServiceInstance
import com.hye.mylibrary.data.room.TargetWordRoomDatabase
import com.hye.sesac.klangpj.BuildConfig

class AppContainer(private val context: Context) {

    // DataStore Manager
    private val dataStoreManager: PreferenceDataStoreManager by lazy {
        PreferenceDataStoreManager(context)
    }

    // Room Database
    private val database: TargetWordRoomDatabase by lazy {
        TargetWordRoomDatabase.getDatabase(context)
    }

    // Retrofit Service
    private val retrofitService by lazy {
        RetrofitServiceInstance.getRetrofitServiceInstance()
    }

    // Repositories
    val wordRepository: WordRepository by lazy {
        WordRepoImpl(
            retrofitService,
            BuildConfig.API_KEY
        )
    }

    val detailWordRepository: DetailWordRepository by lazy {
        DetailWordRepoImpl(
            retrofitService,
            BuildConfig.API_KEY
        )
    }

    private val fireStoreRepository: FireStoreRepository by lazy {
        FireStoreRepositoryImpl(dataStoreManager)
    }

     private val studyRoomRepository: StudyRoomRepository by lazy {
        StudyRoomRepoImpl(database)
    }

    // UseCases
    val loadTodayStudyWordUseCase: LoadTodayStudyWord by lazy {
        LoadTodayStudyWord(
            fireStoreRepository, studyRoomRepository
        )
    }

}