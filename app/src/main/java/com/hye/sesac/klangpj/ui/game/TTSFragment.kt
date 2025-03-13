package com.hye.sesac.klangpj.ui.home

import android.os.Bundle
import android.speech.tts.TextToSpeech
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import com.hye.sesac.klangpj.BaseFragment
import com.hye.sesac.klangpj.common.initTTS
import com.hye.sesac.klangpj.common.readText
import com.hye.sesac.klangpj.common.throttleFirst
import com.hye.sesac.klangpj.databinding.FragmentTtsBinding
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import ru.ldralighieri.corbind.view.clicks

/**
 * tts 사용
 */
class TTSFragment : BaseFragment<FragmentTtsBinding>(FragmentTtsBinding::inflate) {

    private lateinit var tts: TextToSpeech
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        _binding = FragmentTtsBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        tts = initTTS(requireContext())

        with(binding) {
            toSpeechBtn.clicks()
                .throttleFirst(300L)
                .onEach {
                    editTxt.editText?.text?.toString()?.let {
                       tts.readText(it)
                    }
                }
                .launchIn(lifecycleScope)
        }
    }


    //tts 자원해제
    override fun onDestroyView() {
        tts.shutdown()
        super.onDestroyView()

    }
}