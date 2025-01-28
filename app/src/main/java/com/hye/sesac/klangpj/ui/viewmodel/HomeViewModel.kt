package com.hye.sesac.klangpj.ui.viewmodel


import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hye.sesac.data.entity.DetailItem
import com.hye.sesac.data.entity.WordInfo
import com.hye.sesac.data.repo.DetailWordRepoImp
import com.hye.sesac.data.repo.WordRepoImp
import com.hye.sesac.data.rest.RetrofitServiceInstance
import com.hye.mylibrary.data.model.FireStoreWords
import com.hye.sesac.domain.firestore.repo.FireStoreRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import com.hye.sesac.klangpj.BuildConfig

class HomeViewModel(
    // private val fireStoreRepository: FireStoreRepository = FireStoreRepoImp()
    private val wordRepository: WordRepoImp = WordRepoImp(
        RetrofitServiceInstance.getRetrofitServiceInstance(),
        BuildConfig.API_KEY
    ),
    private val detailWordRepository: DetailWordRepoImp = DetailWordRepoImp(
        RetrofitServiceInstance.getRetrofitServiceInstance(),
        BuildConfig.API_KEY
    ),
    private val fireStoreRepository: FireStoreRepository = FireStoreRepository()
) : ViewModel() {
    //단어 검색
    private var _wordInfo = MutableStateFlow<List<WordInfo>>(emptyList())
    val wordInfo = _wordInfo.asStateFlow()

    //단어 상세 정보
    private var _detailWordInfo = MutableStateFlow<List<DetailItem>>(emptyList())
    val detailWordInfo = _detailWordInfo.asStateFlow()

    //firebase 정보
    private var _fireStoreInfo = MutableStateFlow<List<FireStoreWords>>(emptyList())
    val fireStoreInfo = _fireStoreInfo.asStateFlow()


    //단어검색
    fun getWordInfo(inputWord: String) {
        viewModelScope.launch {
            try {
                val response = wordRepository.getWordInfo(inputWord).item
                response?.let {
                    _wordInfo.value = it
                }


            } catch (e: Exception) {
                // 에러 로깅
                // Log.e("API_ERROR", "Error: ${e.message}")
                _wordInfo.value = emptyList()

            }
        }
    }

    //상세 정보
    fun getDetailWordInfo(targetCode: String) {
        viewModelScope.launch {
            try {
                val detailResponse = detailWordRepository.getDetailWordInfo(targetCode).items
                detailResponse?.let {
                    _detailWordInfo.value = it
                }

            } catch (e: Exception) {

            }
        }
    }

    fun getFireStoreInfo(count: Long) {
        viewModelScope.launch {
            try {
                val fireStoreResponse = fireStoreRepository.testConnection(count)
                Log.e("Firebase",fireStoreResponse.size.toString())
                   _fireStoreInfo.value = fireStoreResponse

                }catch (e:Exception){
                    Log.e("FireStoreError", "Error: ${e.message}")

            }
        }
    }
}





