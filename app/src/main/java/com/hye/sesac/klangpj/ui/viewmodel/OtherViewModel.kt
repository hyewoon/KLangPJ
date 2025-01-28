package com.hye.sesac.klangpj.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hye.sesac.domain.firestore.repo.FireStoreUploadRepo
import com.hye.sesac.klangpj.BuildConfig
import com.hye.sesac.klangpj.common.KLangApplication
import kotlinx.coroutines.launch

class OtherViewModel(
    private val dataUploadRepository: FireStoreUploadRepo = FireStoreUploadRepo(
        KLangApplication.getKLangContext(), BuildConfig.API_KEY
    ),
) : ViewModel() {

    fun checkAndUploadData() {
        viewModelScope.launch {
            if (!dataUploadRepository.isExcelTaskCompleted()) {
                dataUploadRepository.upLoadExcelToFireStore()
                dataUploadRepository.markAllTasksAsCompleted()
            }else if(!dataUploadRepository.isApiTaskCompleted()){
                dataUploadRepository.upLoadApiToFireStore()
                dataUploadRepository.markAllApiTasksAsCompleted()
            }
        }
    }
}