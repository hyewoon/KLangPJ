package com.hye.sesac.klangpj.ui.game

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.hye.domain.result.MLKitResult
import com.hye.sesac.klangpj.BaseFragment
import com.hye.sesac.klangpj.common.showDialog
import com.hye.sesac.klangpj.databinding.FragmentDrawBinding
import com.hye.sesac.klangpj.ui.viewmodel.GameViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import ru.ldralighieri.corbind.view.clicks

/**
 * mvvm패턴 -> MLKIt
 * 이유? UI에서 그리는 작업은 간단하지만, 이걸 인식하는 과정은 오래걸릴 수 있음 그래서 비동기 작업이 필요함
 * TTS에서도 사용함
 */

class DrawFragment : BaseFragment<FragmentDrawBinding>(FragmentDrawBinding::inflate) {

    private val viewModel by activityViewModels<GameViewModel>()
    private lateinit var inkDialog: AlertDialog


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentDrawBinding.inflate(inflater, container, false)


        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.mlKitResult.collectLatest { result ->
                    when (result) {
                        is MLKitResult.Success -> {
                            if (binding.motionLayout.currentState == binding.motionLayout.startState) {
                                binding.motionLayout.transitionToEnd()
                                binding.recognitionResult.text = result.data
                                binding.inputBtn.text = "되돌아가기"
                            }
                        }


                        is MLKitResult.Failure -> {
                            showDialog("Error: ${result.exception}", requireContext())
                        }

                        else -> {
                        }

                    }
                }


            }
        }


        with(binding) {
            inputBtn.clicks().onEach {
                if (motionLayout.currentState == motionLayout.startState) {
                    drawingView.getCurrentStrokes().let { strokes ->
                        viewModel.recognizeInk(strokes)
                    }
                } else {
                    motionLayout.transitionToStart()
                    inputBtn.text = "입력"
                    recognitionResult.text = ""
                    drawingView.clearCanvas()
                }

            }.launchIn(lifecycleScope)

            eraseBtn.clicks().onEach {
                drawingView.clearCanvas()
            }.launchIn(lifecycleScope)
        }
    }

    override fun onStop() {
        super.onStop()
        viewModel.setMLKitResult(MLKitResult.Initial)
    }

}


