package com.hye.sesac.klangpj.ui.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hye.domain.result.RoomDBResult
import com.hye.domain.usecase.LoadTodayStudyWord
import com.hye.sesac.klangpj.common.KLangApplication
import com.hye.sesac.klangpj.manager.AudioPlayerManager
import com.hye.sesac.klangpj.state.TodayWordsUiState
import com.hye.sesac.klangpj.state.UiStateResult
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch

class HomeViewModel(
    private val sharedViewModel: SharedViewModel,
    private val useCase: LoadTodayStudyWord,
) : ViewModel() {
    private val fireStoreRepository = KLangApplication.firestoreRepository
    private val audioManager: AudioPlayerManager =
        AudioPlayerManager(KLangApplication.getKLangContext())


    private var _todayWordsUiState =
        MutableStateFlow<UiStateResult<List<TodayWordsUiState>>>(UiStateResult.Loading)
    private val todayWordsUiState = _todayWordsUiState.asStateFlow()

    private var _currentIndex = MutableStateFlow(0)
    val currentIndex = _currentIndex.asStateFlow()

    private val _currentWord: MutableStateFlow<UiStateResult<TodayWordsUiState>> =
        MutableStateFlow<UiStateResult<TodayWordsUiState>>(UiStateResult.Loading)
    val currentWord = _currentWord.asStateFlow()

    private var _pronunciationUrl = MutableStateFlow("")
    val pronunciationUrl = _pronunciationUrl.asStateFlow()

    val isAudioPlaying = audioManager.isAudioPlaying



    init {
        viewModelScope.launch {
            //sharedViewModel.resetDailyWordCount()

            _currentWord.value = UiStateResult.Loading
            combine(
                todayWordsUiState,
                currentIndex,
            ) { state, index ->
                when (state) {
                    is UiStateResult.Success -> {
                        Log.d("HomeViewModel", "combine값 받음")
                        val wordItem = state.data.getOrNull(index) ?: TodayWordsUiState()
                        _pronunciationUrl.value = wordItem.pronunciation
                        Log.d("HomeViewModel", "wordItem: ${wordItem.pronunciation}")
                        UiStateResult.Success(wordItem)
                    }
                    //다른 경우 기본값 반환
                    is UiStateResult.Loading -> {
                        UiStateResult.Loading
                    }

                    is UiStateResult.RoomDBFailure -> {
                        UiStateResult.RoomDBFailure("")
                    }

                    else -> {
                        UiStateResult.NetWorkFailure("")

                    }
                }
            }.collectLatest { word ->
                _currentWord.value = word
            }
        }
    }

    fun searchUseCase(targetWord: Int) {
        viewModelScope.launch {
            //이미 로드 된것 있으면 재사용
            if (_todayWordsUiState.value is UiStateResult.Success) {
                Log.d("HomeViewModel", "이미 로드된 데이터 사용")
                return@launch
            }

            _todayWordsUiState.value = UiStateResult.Loading

            when (val response = useCase.invoke(targetWord)) {
                is RoomDBResult.Success -> {
                    runCatching{
                    val uiItems = response.data.map { word ->
                        TodayWordsUiState(
                            wordGrade = word.wordGrade,
                            word = word.korean,
                            english = word.english,
                            pos = word.pos,
                            examples = word.exampleInfo.map { example ->
                                example.example
                            },
                            pronunciation = word.pronunciationInfo.firstOrNull { !it.audioUrl.isNullOrEmpty() }?.audioUrl?:"",
                            isBasicLearned = word.isBasicLearned,
                            isListened = word.isListened,
                            isExampleLearned = word.isExampleLearned,
                            isWritten = word.isWritten,
                            isRecorded = word.isRecorded
                        ).also {
                            Log.d("HomeViewModel", "uiItem_pronunciation: ${it.pronunciation}")

                        }
                    }

                    Log.d("HomeViewModel", "uiItems: ${uiItems.size}")
                    if (uiItems.isNotEmpty()) {
                        Log.d("HomeViewModel", "첫 번째 아이템 샘플: ${uiItems.first()}")
                    }

                    _todayWordsUiState.value = UiStateResult.Success(uiItems)

                }.onFailure { e ->
                // 데이터 변환 과정의 예외 처리
                Log.e("HomeViewModel", "데이터 변환 오류: ${e.message}", e)
                _todayWordsUiState.value = UiStateResult.RoomDBFailure("데이터 변환 오류: ${e.message}")
            }
                }

                is RoomDBResult.RoomDBError -> {
                    Log.e("HomeViewModel", "Room DB 오류: ${response.exception.message}")
                    _todayWordsUiState.value = UiStateResult.RoomDBFailure(response.exception.message ?: "room오류")
                }

                else -> {
                    Log.e("HomeViewModel", "알 수 없는 결과 타입")
                    _todayWordsUiState.value = UiStateResult.NetWorkFailure("알 수 없는 오류가 발생했습니다")

                }

            }
        }
    }

    /**
     * currentState : 단어리스트
     * currentState.data.size : 학습할 단어의 갯수
     */
    fun moveToNextWord() {
        val currentState = _todayWordsUiState.value
        if (currentState is UiStateResult.Success &&
            _currentIndex.value < currentState.data.size
        ) {
            _currentIndex.value += 1

            viewModelScope.launch {
                sharedViewModel.incrementCurrentWordCount()
                sharedViewModel.addPawPoint()
            }

        }
    }

    fun moveToPreviousWord() {
        if (_currentIndex.value > 0) {
            _currentIndex.value -= 1
            _todayWordsUiState.value = _todayWordsUiState.value

            /* viewModelScope.launch {
     sharedViewModel.decrementCurrentWordCount()
 }
*/

        }
    }

    /**
     * hasNextWord(): 마지막 단어 인지 확인 하는 함수
     * hasPreviousWord(): 처음 단어 인지 확인 하는 함수
     */
    fun hasNextWord(): Boolean {
        val currentState = _todayWordsUiState.value
        return if (currentState is UiStateResult.Success) {
            _currentIndex.value < currentState.data.size - 1
        } else false
    }

    fun hasPreviousWord(): Boolean {
        return _currentIndex.value > 0
    }

    fun playAudio(url: String) {
        audioManager.playAudio(url)
    }

    fun stopAudio() {
        audioManager.stopAudio()

    }

    override fun onCleared() {
        super.onCleared()
        audioManager.releasePlayer()
    }

}