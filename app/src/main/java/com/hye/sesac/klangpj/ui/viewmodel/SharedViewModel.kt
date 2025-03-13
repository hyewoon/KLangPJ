package com.hye.sesac.klangpj.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hye.sesac.klangpj.common.KLangApplication
import com.hye.sesac.klangpj.data.preferences.PreferenceDataStoreManager
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
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
    private val _pawCount = MutableStateFlow<Int>(0)
    val pawCount = _pawCount.asStateFlow()

    private val _crownCount = MutableStateFlow<Int>(0)
    val crownCount = _crownCount.asStateFlow()

    init {
        viewModelScope.launch {
            _pawCount.emit(100)
            _crownCount.emit(50)
        }


    }

    suspend fun updatePawCount(count: Int) {
        _pawCount.emit(count)
    }

    suspend fun updateCrownCount(count: Int) {
        _crownCount.emit(count)
    }

    //오늘의 학습 목표
     val targetWordCount = preferenceManager.targetWordCount
        .stateIn(scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(3000),
            initialValue = 10)

    fun updateTargetWordCount(count: Int ){
        viewModelScope.launch {
            preferenceManager.saveTargetWordCount(count)

        }

    }
    suspend fun getTargetWordCount() = targetWordCount.first().toInt()

    //현재 학습 단어
    val currentWordCount = preferenceManager.currentWordCount
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(3000),
            initialValue = 0
        )

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
}