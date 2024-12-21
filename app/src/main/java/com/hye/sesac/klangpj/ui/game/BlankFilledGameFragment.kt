package com.hye.sesac.klangpj.ui.game

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.hye.sesac.klangpj.BaseFragment
import com.hye.sesac.klangpj.databinding.FragmentBlankFilledGameBinding


class BlankFilledGameFragment :
    BaseFragment<FragmentBlankFilledGameBinding>(FragmentBlankFilledGameBinding::inflate) {
    companion object {
        fun newInstance(param1: String, param2: String) =
            BlankFilledGameFragment()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        _binding = FragmentBlankFilledGameBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

    }
}