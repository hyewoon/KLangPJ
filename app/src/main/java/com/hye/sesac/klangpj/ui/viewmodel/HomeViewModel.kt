package com.hye.sesac.klangpj.ui.viewmodel


import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hye.domain.data.HandWritingStroke
import com.hye.domain.data.MLKitResult
import com.hye.domain.repo.MLKitHandwritingRecognizer
import com.hye.sesac.data.entity.DetailItem
import com.hye.sesac.data.entity.WordInfo
import com.hye.sesac.data.repo.DetailWordRepoImpl
import com.hye.sesac.data.repo.WordRepoImpl
import com.hye.sesac.data.rest.RetrofitServiceInstance
import com.hye.mylibrary.data.model.FireStoreWords
import com.hye.mylibrary.data.repo.MLKItRecognizerImpl
import com.hye.sesac.domain.firestore.repo.FireStoreRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import com.hye.sesac.klangpj.BuildConfig

class HomeViewModel(
    // private val fireStoreRepository: FireStoreRepository = FireStoreRepoImp()
    private val wordRepository: WordRepoImpl = WordRepoImpl(
        RetrofitServiceInstance.getRetrofitServiceInstance(),
        BuildConfig.API_KEY
    ),
    private val detailWordRepository: DetailWordRepoImpl = DetailWordRepoImpl(
        RetrofitServiceInstance.getRetrofitServiceInstance(),
        BuildConfig.API_KEY
    ),
    private val fireStoreRepository: FireStoreRepository = FireStoreRepository()
) : ViewModel() {
    private val recognizer: MLKitHandwritingRecognizer = MLKItRecognizerImpl.MLKitRecognitionFactory.create()


    //단어 검색
    private var _wordInfo = MutableStateFlow<List<WordInfo>>(emptyList())
    val wordInfo = _wordInfo.asStateFlow()

    //단어 상세 정보
    private var _detailWordInfo = MutableStateFlow<List<DetailItem>>(emptyList())
    val detailWordInfo = _detailWordInfo.asStateFlow()

    //firebase 정보
    private var _fireStoreInfo = MutableStateFlow<List<FireStoreWords>>(emptyList())
    val fireStoreInfo = _fireStoreInfo.asStateFlow()

    //핸드라이트 인식
    private var _recognizedInk = MutableStateFlow<MLKitResult<String>>(MLKitResult.Initial)
    val recognizedInk = _recognizedInk.asStateFlow()


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

    fun recognizeInk(strokes: List<HandWritingStroke>) {
        viewModelScope.launch {
            try {
                val result = recognizer.recognize(strokes)
               // _recognizedInk.value = result
                Log.d("ViewModel", "stroke 개수: ${strokes.size} ")
                when(result){
                    is MLKitResult.Success ->
                       _recognizedInk.value = result

                    is MLKitResult.Failure ->
                        _recognizedInk.value = result

                    else -> _recognizedInk.value = result
            }



            }catch (e:Exception){

            }


        }
    }
    override fun onCleared() {
        super.onCleared()
          // 리소스 해제
        recognizer.cleanUp()
    }



}





