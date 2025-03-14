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
import com.hye.sesac.klangpj.common.TTSPlay
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
    private var transitionStarted: Boolean = false


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentDrawBinding.inflate(inflater, container, false)


        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        TTSPlay.readText("", requireContext())

        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.mlKitResult.collectLatest { result ->
                    when (result) {
                        is MLKitResult.Success -> {
                            binding.recognitionResult.text = result.data
                            binding.motionLayout.transitionToEnd()
                            binding.inputBtn.text = "되돌아가기"


                        }


                        /*      if(result.data.isValidRecognition){
                                  binding.recognitionResult.text = result.data.recognizedText
                                  binding.confidenceTv.text =(result.data.confidence * 100).toInt().toString() + "%"


                                  TTSPlay.readText(result.data.recognizedText, requireContext())
                              }else{
                                  binding.confidenceTv.text =(result.data.confidence * 100).toInt().toString() + "%"
                                  binding.recognitionResult.text = " 다시 인식해 주세요"

                              }*/
                        is MLKitResult.Failure -> {
                            showDialog("Error: ${result.exception}", requireContext())
                        }

                        MLKitResult.Initial -> {
                        }
                    }
                }


            }
        }


        with(binding) {
            inputBtn.clicks().onEach {
                //viewModel로 데이터를 보냄
               drawingView.getCurrentStrokes().let { strokes ->
                   viewModel.recognizeInk(strokes)

               }

            }.launchIn(lifecycleScope)

            eraseBtn.clicks().onEach {
                drawingView.clearCanvas()
            }.launchIn(lifecycleScope)
        }
    }


    override fun onStop() {
        super.onStop()
        if (::inkDialog.isInitialized && inkDialog.isShowing) {
            inkDialog.dismiss()
        }
        viewModel.setMLKitResult(MLKitResult.Initial)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        TTSPlay.release()
    }

    // 버튼 클릭 리스너 설정 함수
    private fun setupButtonClickListener() {
        with(binding) {
            if (motionLayout.currentState == motionLayout.endState) {
                // 현재 절반 화면 상태이면 전체 화면으로 전환
                motionLayout.transitionToStart()
                inputBtn.text = "입력"
            } else {
                // 현재 전체 화면 상태이면 절반 화면으로 전환
                //viewModel로 데이터를 보냄
                drawingView.getCurrentStrokes().let { strokes ->
                    viewModel.recognizeInk(strokes)
                    //drawingView.clearCanvas()
                    binding.motionLayout.transitionToEnd()
                    binding.inputBtn.text = "되돌아가기"

                }


            }
        }
    }

}


