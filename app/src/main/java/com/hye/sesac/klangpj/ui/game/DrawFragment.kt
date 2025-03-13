package com.hye.sesac.klangpj.ui.home

import android.os.Bundle
import android.speech.tts.TextToSpeech
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.hye.domain.result.MLKitResult
import com.hye.sesac.klangpj.BaseFragment
import com.hye.sesac.klangpj.common.initTTS
import com.hye.sesac.klangpj.common.readText
import com.hye.sesac.klangpj.databinding.FragmentDrawBinding
import com.hye.sesac.klangpj.ui.factory.ViewModelFactory
import com.hye.sesac.klangpj.ui.viewmodel.HomeViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import ru.ldralighieri.corbind.view.clicks


/**
 * mvvm패턴 -> MLKIt
 * 이유? UI에서 그리는 작업은 간단하지만, 이걸 인식하는 과정은 오래걸릴 수 있음 그래서 비동기 작업이 필요함
 */
class DrawFragment : BaseFragment<FragmentDrawBinding>(FragmentDrawBinding::inflate) {
    private lateinit var tts: TextToSpeech

    //private val viewModel by activityViewModels<HomeViewModel> {
    // ViewModelFactory()
    //}
    private val viewModel by activityViewModels<HomeViewModel>()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentDrawBinding.inflate(inflater, container, false)
        ///값 읽어오기
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.mlKitResult.collectLatest { result ->
                    when (result) {
                        is MLKitResult.Success -> {
                            Log.e("TAG", "${result.data}")
                            val text = result.data
                            showDialog( result.data)
                            tts.readText(text)

                        }

                        is MLKitResult.Failure -> {
                            showDialog("Error: ${result.exception}")
                        }

                        MLKitResult.Initial -> {
                        }
                    }

                }

            }
        }
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        tts = initTTS(requireContext())

        with(binding) {
            inputBtn.clicks()
                .onEach {
                    //viewModel로 데이터를 보냄
                    drawingView.getCurrentStrokes().let { strokes ->
                        viewModel.recognizeInk(strokes)
                        drawingView.clearCanvas()
                    }

                }
                .launchIn(lifecycleScope)

            eraseBtn.clicks()
                .onEach {
                    drawingView.clearCanvas()
                }
                .launchIn(lifecycleScope)
        }
    }


    private fun showDialog(result: String) {
        MaterialAlertDialogBuilder(
            requireContext(),
            com.google.android.material.R.style.ThemeOverlay_Material3_MaterialAlertDialog
        )
            .setTitle("분석 결과")
            .setMessage(result)
            .setPositiveButton("확인") { dialog, _ ->
                dialog.dismiss()
            }
            .show()


    }

    override fun onDestroyView() {
        tts?.let {
            it.stop()
            it.shutdown()
        }
        super.onDestroyView()
    }


}