package com.hye.sesac.klangpj.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import com.hye.sesac.klangpj.BaseFragment
import com.hye.sesac.klangpj.databinding.FragmentDrawBinding
import com.hye.sesac.klangpj.ui.factory.viewModelFactory
import com.hye.sesac.klangpj.ui.model.HomeViewModel


/**
 * google vision API로 구현
 */
class DrawFragment : BaseFragment<FragmentDrawBinding>(FragmentDrawBinding::inflate) {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        _binding = FragmentDrawBinding.inflate(inflater, container, false)
        return binding.root
    }

    private val viewModel by activityViewModels<HomeViewModel> {
        viewModelFactory
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


    }

}