package com.hye.sesac.klangpj.ui.home.word

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import com.hye.sesac.klangpj.BaseFragment
import com.hye.sesac.klangpj.databinding.FragmentWriteDownBinding
import com.hye.sesac.klangpj.ui.factory.viewModelFactory
import com.hye.sesac.klangpj.ui.model.HomeViewModel

/**
 * google Vision API로 구현
 */
class WriteDownFragment :
    BaseFragment<FragmentWriteDownBinding>(FragmentWriteDownBinding::inflate) {

    companion object {
        fun newInstance() =
            WriteDownFragment()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        _binding = FragmentWriteDownBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    private val viewModel by activityViewModels<HomeViewModel> {
        viewModelFactory
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

    }

}