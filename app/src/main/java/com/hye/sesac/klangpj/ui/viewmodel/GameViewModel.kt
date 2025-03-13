package com.hye.sesac.klangpj.ui.viewmodel


import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hye.domain.model.DetailWordEntity
import com.hye.domain.model.HandWritingStroke
import com.hye.domain.repo.MLKitRepository
import com.hye.domain.model.WordEntity
import com.hye.domain.result.ApiResult
import com.hye.domain.result.FirebaseResult
import com.hye.domain.result.MLKitResult
import com.hye.mylibrary.data.repo.MLKItRecognizerImpl
import com.hye.sesac.klangpj.common.KLangApplication
import com.hye.sesac.klangpj.state.TodayWordUiState
import com.hye.sesac.klangpj.state.UiStateResult
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class HomeViewModel : ViewModel() {
    private val wordRepository = KLangApplication.wordRepository
    private val detailWordRepository = KLangApplication.detailWordRepository
    private val mlkitRepository = MLKItRecognizerImpl.create()
    private val fireStoreRepository = KLangApplication.firestoreRepository

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

    //firebase 정보
    private var _todayWordUiState =
        MutableStateFlow<UiStateResult<List<TodayWordUiState>>>(UiStateResult.Loading)
    val todayWordUiState = _todayWordUiState.asStateFlow()

    private var _currentIndex = MutableStateFlow(0)
    val currentIndex = _currentIndex.asStateFlow()

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

    fun getFireStoreInfo(count: Long) {
        viewModelScope.launch {
            _todayWordUiState.value = UiStateResult.Loading
            //1. 학습할 단어갯수를 보낸다.
            when (val response = fireStoreRepository.getFireStoreWord(count)) {
                is FirebaseResult.Success -> {
                    _todayWordUiState.value = UiStateResult.Success(

                        response.data.map {

                            Log.d("viewModel", "getFireStoreInfo: ${response.data}")
                            TodayWordUiState(
                                word = it.korean,
                                english = it.english,
                                pos = it.pos,
                                wordGrade = it.wordGrade ?: "",
                                examples = it.exampleInfo?.map { example ->
                                    example.example
                                        .replace("[", "")
                                        .replace("]", "")
                                        .split(",")
                                        .filter { it.isNotBlank() }
                                }?.flatten() ?: emptyList()
                            )
                        }
                    )
                }


                is FirebaseResult.NetWorkFailure ->
                    _todayWordUiState.value = UiStateResult.NetWorkFailure("")

                is FirebaseResult.RoomDBFailure -> UiStateResult.RoomDBFailure("")
                else -> {}


            }
        }
    }

    fun moveToNextWord() {
        val currentState = _todayWordUiState.value
        if (currentState is UiStateResult.Success &&
            _currentIndex.value < currentState.data.size - 1
        ) {
            _currentIndex.value += 1

        }
    }

    fun moveToPreviousWord() {
        if (_currentIndex.value > 0) {
            _currentIndex.value -= 1
            _todayWordUiState.value = _todayWordUiState.value
        }
    }

    fun hasNextWord(): Boolean {
        val currentState = _todayWordUiState.value
        return if (currentState is UiStateResult.Success) {
            _currentIndex.value < currentState.data.size - 1
        } else false
    }

    fun hasPreviousWord(): Boolean {
        return _currentIndex.value > 0
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





