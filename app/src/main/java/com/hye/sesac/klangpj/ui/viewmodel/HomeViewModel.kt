package com.hye.sesac.klangpj.ui.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hye.domain.result.RoomDBResult
import com.hye.domain.usecase.LoadTodayStudyWord
import com.hye.sesac.klangpj.common.KLangApplication
import com.hye.sesac.klangpj.state.TodayWordUiState
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

    private var _todayWordUiState =
        MutableStateFlow<UiStateResult<List<TodayWordUiState>>>(UiStateResult.Loading)
    private val todayWordUiState = _todayWordUiState.asStateFlow()

    private var _currentIndex = MutableStateFlow(0)
    private val currentIndex = _currentIndex.asStateFlow()

    // 현재 선택된 단어를 다른 Fragment들과 공유하기 위한 StateFlow
    private val _currentWordsList = MutableStateFlow<TodayWordUiState?>(null)
    val currentWordsList = _currentWordsList.asStateFlow()

/*
* homeviewModel이 시작될 때,
* todayWordUiState: 오늘의 학습 단어 리스트
* currentIndex: 단어리스트 index값
*
* 두 개의 stateFlow를 zip한다
*
* */

    init {
        viewModelScope.launch {
            sharedViewModel.resetDailyWordCount()
            combine(
                todayWordUiState,
                currentIndex
            ) { state, index ->
                when (state) {
                    is UiStateResult.Success -> state.data.getOrNull(index)
                    else -> null
                }
            }.collectLatest { word ->
                _currentWordsList.value = word
            }
        }
    }

    fun searchUseCase(targetWord: Int) {
        viewModelScope.launch {
            _todayWordUiState.value = UiStateResult.Loading


            val result = useCase.invoke(targetWord)
            Log.d("homeViewModel", "searchUseCase: $result")

            when (val response = useCase.invoke(targetWord)) {
                is RoomDBResult.Success -> {
                    val uiItems = response.data.map {
                        TodayWordUiState(
                            wordGrade = it.wordGrade,
                            word = it.korean,
                            english = it.english,
                            pos = it.pos,
                            examples = it.exampleInfo?.map { example ->
                                example.example
                            } ?: emptyList(),
                            pronunciation = it.pronunciationInfo?.firstOrNull()?.audioUrl
                                ?: "음성 자료가 없습니다."
                        )
                    }
                    _todayWordUiState.value = UiStateResult.Success(uiItems)


                }

                is RoomDBResult.RoomDBError -> {
                    UiStateResult.RoomDBFailure("")
                }

                else -> {}

            }
        }
    }

    /**
     * _currentIndex의 값을 1 증가시키고,
     * shareViewModel의 currentWordCount(호출)
     */
    fun moveToNextWord() {
        val currentState = _todayWordUiState.value
        if (currentState is UiStateResult.Success &&
            _currentIndex.value < currentState.data.size
        ) {
            _currentIndex.value += 1

            viewModelScope.launch {
                sharedViewModel.incrementCurrentWordCount()
            }

        } else if (currentState is UiStateResult.Success && _currentIndex.value >= currentState.data.size) {
            _currentIndex.value = sharedViewModel.targetWordCount.value
        }
    }

    fun moveToPreviousWord() {
        if (_currentIndex.value > 0) {
            _currentIndex.value -= 1
            _todayWordUiState.value = _todayWordUiState.value

          /* viewModelScope.launch {
               sharedViewModel.decrementCurrentWordCount()
           }
*/
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

}