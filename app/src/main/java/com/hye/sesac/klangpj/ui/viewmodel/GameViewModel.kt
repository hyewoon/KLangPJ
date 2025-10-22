package com.hye.sesac.klangpj.ui.viewmodel


import android.content.Intent
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hye.domain.model.DetailWordEntity
import com.hye.domain.model.HandWritingStroke
import com.hye.domain.model.WordEntity
import com.hye.domain.repo.DetailWordRepository
import com.hye.domain.repo.WordRepository
import com.hye.domain.result.ApiResult
import com.hye.domain.result.MLKitResult
import com.hye.mylibrary.data.repo.MLKItRecognizerImpl
import com.hye.sesac.klangpj.common.KLangApplication
import com.hye.sesac.klangpj.manager.STTPlayManager
import com.hye.sesac.klangpj.manager.TTSPlayManager
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class GameViewModel(
    private val wordRepository: WordRepository,
    private val detailWordRepository: DetailWordRepository) : ViewModel()
 {

    private val mlkitRepository = MLKItRecognizerImpl.create()
    private val ttsPlayManager: TTSPlayManager = TTSPlayManager()
    private val sttPlayManager: STTPlayManager = STTPlayManager(KLangApplication.getKLangContext())

    init {
        viewModelScope.launch {
            mlkitRepository.setUpRecognizer()
            ttsPlayManager.readText("",KLangApplication.getKLangContext())
        }
        //setupSTTCallbacks()

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
    fun setMLKitResult(value: MLKitResult.Initial) {
        _mlKitResult.value = value
    }

    val mlKitResult = _mlKitResult.asStateFlow()

    //tts
    fun readText(text: String) {
        ttsPlayManager.readText(text)
    }

    //stt
    private var _sttText = MutableStateFlow("")
    val sttText = _sttText.asStateFlow()

    fun createSTTIntent() = sttPlayManager.createSTTIntent()

    fun handleSTTResult(data: Intent?) {
        val result = sttPlayManager.processSTTResult(data)
        result?.let {
            _sttText.value = it
        }
    }
    fun isSTTAvailable() = sttPlayManager.isSTTAvailable()


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
        ttsPlayManager.release()
        //Laucher방식에서는 불필요
        //sttPlayManager.destroy()
    }

    fun wordInfoClear() {
        _wordInfo.value = ApiResult.DummyConstructor
        _detailWordInfo.value = ApiResult.DummyConstructor
    }

    fun clearSTTText(){
        _sttText.value = ""
    }


}





