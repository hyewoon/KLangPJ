package com.hye.sesac.klangpj.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.hye.sesac.klangpj.BaseFragment
import com.hye.sesac.klangpj.databinding.FragmentToSpeechBinding
import kotlinx.coroutines.flow.onEach
import ru.ldralighieri.corbind.view.clicks

/**
 * mvp패턴
 * tts 사용
 */
class ToSpeechFragment : BaseFragment<FragmentToSpeechBinding>(FragmentToSpeechBinding::inflate) {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        _binding = FragmentToSpeechBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        with(binding){
            toSpeechBtn.clicks()
                .onEach {}
                }

    }
}