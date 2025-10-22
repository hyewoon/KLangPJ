package com.hye.sesac.klangpj.ui.game

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import com.hye.sesac.klangpj.BaseFragment
import com.hye.sesac.klangpj.common.KLangApplication
import com.hye.sesac.klangpj.common.throttleFirst
import com.hye.sesac.klangpj.databinding.FragmentGameBinding
import com.hye.sesac.klangpj.ui.factory.ViewModelFactory
import com.hye.sesac.klangpj.ui.viewmodel.GameViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import ru.ldralighieri.corbind.view.clicks


class GameFragment : BaseFragment<FragmentGameBinding>(FragmentGameBinding::inflate) {

    private val appContainer by lazy {
        (requireActivity().application as KLangApplication).appContainer
    }
    private val viewModel by activityViewModels<GameViewModel> {
        ViewModelFactory(appContainer)
    }
    private lateinit var navController: NavController

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentGameBinding.inflate(layoutInflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        with(binding){
            linearProgressIndicator.apply {
                max = 10
                progress = 4
            }
            currentGameTv.text = "4"
            todayGameTv.text = "/10"
            hintText.clicks()
                .throttleFirst(300L)
                .onEach {
                    hint.visibility = View.VISIBLE
                }
                .launchIn(lifecycleScope)

        }


        binding.hintText.clicks()
            .throttleFirst(300L)
            .onEach {
                binding.hint.visibility = View.VISIBLE
            }
    }


}