package com.hye.sesac.klangpj.ui.viewmodel

import android.util.Log
import android.view.View
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hye.domain.model.TargetWordWithAllInfoEntity
import com.hye.domain.result.FirebaseResult
import com.hye.domain.result.RoomDBResult
import com.hye.domain.usecase.LoadTodayStudyWord
import com.hye.sesac.klangpj.common.KLangApplication
import com.hye.sesac.klangpj.data.preferences.PreferenceDataStoreManager
import com.hye.sesac.klangpj.state.TodayWordUiState
import com.hye.sesac.klangpj.state.UiStateResult
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch

class HomeViewModel(
    private val sharedViewModel: SharedViewModel,
    private val useCase: LoadTodayStudyWord,
) : ViewModel() {
    private val fireStoreRepository = KLangApplication.firestoreRepository

    private var _todayWordUiState =
        MutableStateFlow<UiStateResult<List<TodayWordUiState>>>(UiStateResult.Loading)
    val todayWordUiState = _todayWordUiState.asStateFlow()

    private var _currentIndex = MutableStateFlow(0)
    private val currentIndex = _currentIndex.asStateFlow()

    // 현재 선택된 단어를 다른 Fragment들과 공유하기 위한 StateFlow
    private val _currentWord = MutableStateFlow<TodayWordUiState?>(null)
    val currentWord = _currentWord.asStateFlow()

    init {
        // currentIndex나 todayWordUiState가 변경될 때마다 currentWord 업데이트
        viewModelScope.launch {
            combine(
                todayWordUiState,
                currentIndex
            ) { state, index ->
                when (state) {
                    is UiStateResult.Success -> state.data.getOrNull(index)
                    else -> null
                }
            }.collect { word ->
                _currentWord.value = word
            }
        }

        viewModelScope.launch {
            sharedViewModel.currentWordCount.collect { count ->
                _currentIndex.value = count
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
                    val uiItems = response.data.map{
                        TodayWordUiState(
                            wordGrade = it.wordGrade,
                            word = it.korean,
                            english = it.english,
                            pos = it.pos,
                            examples = it.exampleInfo?.map { example ->
                                example.example
                            }?:emptyList(),
                            pronunciation = it.pronunciationInfo?.firstOrNull()?.audioUrl ?: "음성 자료가 없습니다."
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

    fun moveToNextWord() {
        val currentState = _todayWordUiState.value
        if (currentState is UiStateResult.Success &&
            _currentIndex.value < currentState.data.size - 1
        ) {
            _currentIndex.value += 1

            viewModelScope.launch {
                sharedViewModel.incrementCurrentWordCount()
            }

        }
    }

    fun moveToPreviousWord() {
        if (_currentIndex.value > 0) {
            _currentIndex.value -= 1
            _todayWordUiState.value = _todayWordUiState.value

            viewModelScope.launch {
                sharedViewModel.decrementCurrentWordCount()
            }
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