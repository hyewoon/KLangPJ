package com.hye.sesac.klangpj.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.hye.sesac.klangpj.BaseFragment
import com.hye.sesac.klangpj.databinding.FragmentDetailDictionaryBinding

class DetailDictionaryFragment : BaseFragment<FragmentDetailDictionaryBinding>(FragmentDetailDictionaryBinding::inflate) {
    private lateinit var navController: NavController
    private val args : DetailDictionaryFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        _binding = FragmentDetailDictionaryBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navController = findNavController()
        val target = args.targetCode
        /**
         * api 호출
         */

    }

}