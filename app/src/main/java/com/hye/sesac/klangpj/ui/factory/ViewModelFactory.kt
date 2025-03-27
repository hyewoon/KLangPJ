package com.hye.sesac.klangpj.ui.factory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.hye.domain.usecase.LoadTodayStudyWord
import com.hye.sesac.klangpj.ui.viewmodel.HomeViewModel
import com.hye.sesac.klangpj.ui.viewmodel.GameViewModel
import com.hye.sesac.klangpj.ui.viewmodel.SharedViewModel

@Suppress("UNCHECKED_CAST")
class ViewModelFactory(
    private val sharedViewModelProvider: () -> SharedViewModel = { SharedViewModel() },
    private val useCaseProvider: (() -> LoadTodayStudyWord)? = null,

    ) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T =
        with(modelClass) {
            when {
                isAssignableFrom(GameViewModel::class.java) ->
                    GameViewModel()

                isAssignableFrom(HomeViewModel::class.java) -> {
                    // SharedViewModel을 HomeViewModel에 주입
                    val sharedViewModel = sharedViewModelProvider()
                    val useCase = useCaseProvider?.let { it() }
                    useCase?.let {
                        HomeViewModel(sharedViewModel, it)
                    }

                }
                isAssignableFrom(SharedViewModel::class.java) ->
                    SharedViewModel()

                else ->
                    throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
            }
        } as T
}