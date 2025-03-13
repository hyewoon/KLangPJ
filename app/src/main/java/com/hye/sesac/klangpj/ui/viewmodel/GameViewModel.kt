package com.hye.sesac.klangpj.ui.viewmodel


import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hye.domain.model.DetailWordEntity
import com.hye.domain.model.HandWritingStroke
import com.hye.domain.model.MLKitRecognitionResult
import com.hye.domain.model.WordEntity
import com.hye.domain.result.ApiResult
import com.hye.domain.result.MLKitResult
import com.hye.mylibrary.data.repo.MLKItRecognizerImpl
import com.hye.sesac.klangpj.common.KLangApplication
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class GameViewModel : ViewModel() {
    private val wordRepository = KLangApplication.wordRepository
    private val detailWordRepository = KLangApplication.detailWordRepository
    private val mlkitRepository = MLKItRecognizerImpl.create()

    init {
        viewModelScope.launch {
          mlkitRepository.setUpRecognizer()
        }
    }

    //단어 검색
    private var _wordInfo =
        MutableStateFlow<ApiResult<List<WordEntity>>>(ApiResult.DummyConstructor)
    val wordInfo = _wordInfo.asStateFlow()

    //단어 상세 정보
    private var _detailWordInfo =
        MutableStateFlow<ApiResult<List<DetailWordEntity>>>(ApiResult.DummyConstructor)
    val detailWordInfo = _detailWordInfo.asStateFlow()


    //mlkit 인식
    private val _mlKitResult = MutableStateFlow<MLKitResult<String>>(MLKitResult.Initial)
    fun setMLKitResult(value: MLKitResult.Initial){
        _mlKitResult.value = value
    }
    val mlKitResult = _mlKitResult.asStateFlow()


    //단어 검색
    fun getWordInfo(inputWord: String) {
        viewModelScope.launch {
            val response = wordRepository.getWordInfo(inputWord)
            response.let {
                _wordInfo.value = it
            }
        }

    }

    fun formatWordInfo(wordEntity: WordEntity): String {
        return wordEntity.senses.take(3).joinToString("\n\n") {
            "${it.senseOrder}. ${it.transWord}\n ${it.definition}"

        }
    }


    //단어 상세 정보
    fun getDetailWordInfo(targetCode: String) {
        viewModelScope.launch {
            val detailResponse = detailWordRepository.getDetailWordInfo(targetCode)
            detailResponse?.let {
                _detailWordInfo.value = it
            }

        }
    }


    fun recognizeInk(strokes: List<HandWritingStroke>) {
        viewModelScope.launch {
            //인식 실행
            mlkitRepository.recognize(strokes).collectLatest {
                         _mlKitResult.value = it
            }

        }
    }

    override fun onCleared() {
        super.onCleared()
        // 리소스 해제
        mlkitRepository.cleanUp()
    }

    fun wordInfoClear() {
        _wordInfo.value = ApiResult.DummyConstructor
        _detailWordInfo.value = ApiResult.DummyConstructor
    }


}





