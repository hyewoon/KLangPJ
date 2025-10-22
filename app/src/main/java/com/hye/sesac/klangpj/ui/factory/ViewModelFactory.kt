package com.hye.sesac.klangpj.ui.factory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.hye.sesac.klangpj.ui.dicontainer.AppContainer
import com.hye.sesac.klangpj.ui.viewmodel.HomeViewModel
import com.hye.sesac.klangpj.ui.viewmodel.GameViewModel
import com.hye.sesac.klangpj.ui.viewmodel.SharedViewModel

@Suppress("UNCHECKED_CAST")
class ViewModelFactory(
    private val appContainer: AppContainer,
    private val sharedViewModel: SharedViewModel? = null

    ) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T =
        with(modelClass) {
            when {
                isAssignableFrom(GameViewModel::class.java) ->
                    GameViewModel(
                        wordRepository = appContainer.wordRepository,
                        detailWordRepository = appContainer.detailWordRepository)

                isAssignableFrom(HomeViewModel::class.java) -> {
                    val shared = sharedViewModel ?: SharedViewModel()
                    HomeViewModel(
                        sharedViewModel = shared,
                        useCase = appContainer.loadTodayStudyWordUseCase
                    )
                }
                isAssignableFrom(SharedViewModel::class.java) ->
                    SharedViewModel()

                else ->
                    throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
            }
        } as T
}