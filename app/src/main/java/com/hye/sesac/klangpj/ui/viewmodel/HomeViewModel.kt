package com.hye.sesac.klangpj.ui.viewmodel


import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hye.sesac.data.entity.WordInfo
import com.hye.sesac.data.repo.WordRepoImp
import com.hye.sesac.data.rest.RetrofitServiceInstance
import com.hye.sesac.klangpj.BuildConfig
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch


class HomeViewModel(
    // private val fireStoreRepository: FireStoreRepository = FireStoreRepoImp()
    private val wordRepository: WordRepoImp = WordRepoImp(
        RetrofitServiceInstance.getRetrofitServiceInstance(),
        BuildConfig.API_KEY
    ),
) : ViewModel() {

    private var _wordInfo = MutableStateFlow<List<WordInfo>>(emptyList())

    //only_read, UI에서 접근할 수 있도록 public
    var wordInfo = _wordInfo.asStateFlow()

    fun getWordInfo(inputWord: String) {
        viewModelScope.launch {
            try {
                val response = wordRepository.getWordInfo(inputWord)
                _wordInfo.value = response.item ?: emptyList()
            } catch (e: Exception) {
                // 에러 로깅
                Log.e("API_ERROR", "Error: ${e.message}")
                _wordInfo.value = emptyList()

            }
        }
    }
}






