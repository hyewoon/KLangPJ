package com.hye.sesac.klangpj.ui.home.word

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import com.hye.sesac.klangpj.BaseFragment
import com.hye.sesac.klangpj.databinding.FragmentDictionaryBinding
import com.hye.sesac.klangpj.ui.factory.viewModelFactory
import com.hye.sesac.klangpj.ui.model.HomeViewModel


class DictionaryFragment : BaseFragment<FragmentDictionaryBinding>(FragmentDictionaryBinding::inflate) {

    companion object {

        fun newInstance() =
            DictionaryFragment()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        _binding = FragmentDictionaryBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    private val viewModel by activityViewModels<HomeViewModel> {
        viewModelFactory
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)




    }
}