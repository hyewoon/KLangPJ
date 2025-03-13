package com.hye.sesac.klangpj.ui.game

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import com.hye.sesac.klangpj.BaseFragment
import com.hye.sesac.klangpj.MainActivity
import com.hye.sesac.klangpj.R
import com.hye.sesac.klangpj.common.throttleFirst
import com.hye.sesac.klangpj.databinding.FragmentWordGameBinding
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import ru.ldralighieri.corbind.view.clicks


class WordGameFragment : BaseFragment<FragmentWordGameBinding>(FragmentWordGameBinding::inflate) {
    private lateinit var navController: NavController


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        _binding = FragmentWordGameBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        navController = findNavController()
        with(binding) {
            drawCardView.clicks()
                .throttleFirst(300L)
                .onEach {
                    navController.navigate(R.id.drawFragment)
                }
                .launchIn(lifecycleScope)
            searchCardView.clicks()
                .throttleFirst(300L)
                .onEach {
                    navController.navigate(R.id.dictionaryFragment)
                }
                .launchIn(lifecycleScope)
            myDictionaryCardView.clicks()
                .throttleFirst(300L)
                .onEach {  }
                .launchIn(lifecycleScope)
            ttsCardView.clicks()
                .throttleFirst(300L)
                .onEach {
                    navController.navigate(R.id.ttsFragment)
                }
                .launchIn(lifecycleScope)
            sttCardView.clicks()
                .throttleFirst(300L)
                .onEach {
                    navController.navigate(R.id.sttFragment)
                }
                .launchIn(lifecycleScope)

          /*  blankFilledGameCardView.setOnClickListener {
                navController.navigate(R.id.blankFilledGameFragment)
            }
            chooseCorrectWordGameCardView.setOnClickListener {
                navController.navigate(R.id.chooseWordGameFragment)
            }*/
        }

    }
}