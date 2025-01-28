package com.hye.sesac.klangpj.ui.factory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.hye.sesac.klangpj.ui.viewmodel.GameViewModel
import com.hye.sesac.klangpj.ui.viewmodel.HomeViewModel
import com.hye.sesac.klangpj.ui.viewmodel.MyPageViewModel
import com.hye.sesac.klangpj.ui.viewmodel.RankViewModel

@Suppress("UNCHECKED_CAST")
class  ViewModelFactory(
) : ViewModelProvider.Factory {
    override   fun < T : ViewModel>  create(modelClass: Class<T>): T =
        with(modelClass) {
            when {
                isAssignableFrom(HomeViewModel::class.java) ->
                    HomeViewModel()
                isAssignableFrom(GameViewModel::class.java) ->
                    GameViewModel()
                isAssignableFrom(RankViewModel::class.java) ->
                    RankViewModel()
                isAssignableFrom(MyPageViewModel::class.java) ->
                    MyPageViewModel()
                else ->
                    throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
            }
        } as T
}