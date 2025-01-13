package com.hye.sesac.klangpj.ui.game

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.hye.sesac.klangpj.BaseFragment
import com.hye.sesac.klangpj.databinding.FragmentChooseWordGameBinding

class ChooseWordGameFragment :
    BaseFragment<FragmentChooseWordGameBinding>(FragmentChooseWordGameBinding::inflate) {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        _binding = FragmentChooseWordGameBinding.inflate(inflater, container, false)
        return binding.root
    }
}