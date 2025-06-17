package com.hye.sesac.klangpj.ui.game

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.hye.domain.result.MLKitResult
import com.hye.sesac.klangpj.BaseFragment
import com.hye.sesac.klangpj.manager.TTSPlayManager
import com.hye.sesac.klangpj.common.showDialog
import com.hye.sesac.klangpj.common.throttleFirst
import com.hye.sesac.klangpj.databinding.FragmentTtsBinding
import com.hye.sesac.klangpj.ui.viewmodel.GameViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import ru.ldralighieri.corbind.view.clicks

/**
 * tts 사용
 */
class TTSFragment : BaseFragment<FragmentTtsBinding>(FragmentTtsBinding::inflate) {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        _binding = FragmentTtsBinding.inflate(layoutInflater, container, false)

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED){
                viewModel.mlKitResult.collectLatest { result->
                    when (result) {
                        is MLKitResult.Success -> {
                            val text = result.data
                            showDialog(text, requireContext())
                            TTSPlayManager.readText(text)
                        }

                        is MLKitResult.Failure -> {
                            showDialog("Error: ${result.exception}", requireContext())
                        }

                        MLKitResult.Initial -> {
                        }
                    }
                }

            }
        }
        return binding.root
    }

    private val viewModel by activityViewModels<GameViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        TTSPlayManager.readText("", requireContext())  // 빈 텍스트로 초기화만 수행



        with(binding) {
            switchMode.clicks().onEach {
                when(switchMode.isChecked) {
                    true -> {
                        drawingCardView.visibility = View.VISIBLE
                        inputTextLayout.visibility = View.GONE

                        // 키보드 숨기기
                        val imm =
                            requireContext().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                        imm.hideSoftInputFromWindow(editTv.windowToken, 0)

                        // EditText 포커스 제거
                        editTv.clearFocus()
                    }

                    false -> {
                        drawingCardView.visibility = View.INVISIBLE
                        inputTextLayout.visibility = View.VISIBLE
                    }
                }

            }.launchIn(lifecycleScope)


            editTv.clicks()
                .throttleFirst(300L)
                .onEach {
                    val result = editTv.text.toString()
                    TTSPlayManager.readText(result)
                }
                .launchIn(lifecycleScope)

            speechBtn.clicks()
                .throttleFirst(300L)
                .onEach {
                    //viewModel로 데이터를 보냄
                    customView.getCurrentStrokes().let { strokes ->
                        viewModel.recognizeInk(strokes)
                        customView.clearCanvas()
                    }

                }
                .launchIn(lifecycleScope)


        }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        TTSPlayManager.release()

    }
    override fun onStop() {
        super.onStop()
      /*  if (::inkDialog.isInitialized && inkDialog.isShowing) {
            inkDialog.dismiss()
        }*/
        viewModel.setMLKitResult(MLKitResult.Initial)
    }

}