package com.hye.sesac.klangpj.ui.game

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import com.hye.sesac.klangpj.BaseFragment
import com.hye.sesac.klangpj.MainActivity
import com.hye.sesac.klangpj.R
import com.hye.sesac.klangpj.databinding.FragmentWordGameBinding


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
            blankFilledGameCardView.setOnClickListener {
                navController.navigate(R.id.blankFilledGameFragment)
            }
            chooseCorrectWordGameCardView.setOnClickListener {
                navController.navigate(R.id.chooseWordGameFragment)
            }
        }

    }
}