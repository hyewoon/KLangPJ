package com.hye.sesac.klangpj.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hye.sesac.klangpj.common.KLangApplication
import com.hye.mylibrary.data.preferences.PreferenceDataStoreManager
import com.hye.sesac.klangpj.state.TodayWordUiState
import com.hye.sesac.klangpj.state.UiStateResult
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch


/**
 * datastore
 * 1.
 */
class SharedViewModel : ViewModel() {
    private val preferenceManager : PreferenceDataStoreManager = PreferenceDataStoreManager(KLangApplication.getKLangContext())

    /*
    * targetWordCount : 오늘의 목표 단어 학습 갯수
    * currentWordCount : 현재 학습 한 다어 갯수
    * */
    val targetWordCount = preferenceManager.targetWordCount
        .stateIn(scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(3000),
            initialValue = 10)

    val currentWordCount = preferenceManager.currentWordCount
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(3000),
            initialValue = 0
        )

    fun updateTargetWordCount(count: Int ){
        viewModelScope.launch {
            preferenceManager.saveTargetWordCount(count)
        }
    }


    fun incrementCurrentWordCount(){
        viewModelScope.launch {
            preferenceManager.incrementCurrentWordCount()
        }
    }

    fun decrementCurrentWordCount(){
        viewModelScope.launch {
            preferenceManager.decrementCurrentWordCount()
        }
    }

    /**
     * 누적 학습 단어 포인트 적립
     * pawPoint: 누적 학습 단어 갯수 1씩 증가
     * targetPoint : 누적 학습 포인트 2씩 증가
     */
    private val _pawPoint: StateFlow<Int> = preferenceManager.pawPoint
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(3000),
            initialValue = 0
        )

    val pawPoint : StateFlow<Int> = _pawPoint


    private val _targetPoint : StateFlow<Int> = preferenceManager.targetPoint
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(3000),
            initialValue = 0
        )

    val targetPoint : StateFlow<Int> = _targetPoint


    fun addPawPoint(){
        viewModelScope.launch {
            preferenceManager.addPawPoint()
        }
    }

    fun addTargetPoint(){
        viewModelScope.launch {
            preferenceManager.addTargetPoint()
        }
    }


    private val _targetCount = MutableStateFlow<Int>(0)

    val targetCount = _targetCount.asStateFlow()
/*
    init {
        viewModelScope.launch {
            _pawCount.emit(0)
            _targetCount.emit(50)
        }
    }*/

/*
    suspend fun updatePawCount() {
        _pawCount.value += 2
        _pawCount.emit(_pawCount.value)
    }
*/

    suspend fun updateCrownCount(count: Int) {
        _targetCount.emit(count)
    }


    suspend fun getTargetWordCount() = targetWordCount.first().toInt()


}