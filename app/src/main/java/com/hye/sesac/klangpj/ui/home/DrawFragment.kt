package com.hye.sesac.klangpj.ui.home

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.hye.domain.data.MLKitResult
import com.hye.sesac.klangpj.BaseFragment
import com.hye.sesac.klangpj.common.KLangApplication
import com.hye.sesac.klangpj.databinding.FragmentDrawBinding
import com.hye.sesac.klangpj.ui.factory.ViewModelFactory
import com.hye.sesac.klangpj.ui.viewmodel.HomeViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import ru.ldralighieri.corbind.view.clicks


/**
 * mvvm패턴 -> MLKIt
 * 이유? UI에서 그리는 작업은 간단하지만, 이걸 인식하는 과정은 오래걸릴 수 있음 그래서 비동기 작업이 필요함
 */
class DrawFragment : BaseFragment<FragmentDrawBinding>(FragmentDrawBinding::inflate) {

    private val viewModel by activityViewModels<HomeViewModel> {
        ViewModelFactory()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        _binding = FragmentDrawBinding.inflate(inflater, container, false)





        return binding.root
    }


      override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
           super.onViewCreated(view, savedInstanceState)

          viewLifecycleOwner.lifecycleScope.launch {
              viewModel.recognizedInk.collectLatest { result->
                  when(result){
                       is MLKitResult.Success -> {
                           showDialog(result.data)
                       }
                      is MLKitResult.Failure -> {
                          showDialog("Error: ${result.exception}")
                      }
                      MLKitResult.Initial -> {

                      }
                  }
              }
          }

           with(binding) {
               inputBtn.clicks()
                   .onEach {
                       //viewModel로 데이터를 보냄
                     drawingView.getCurrentStrokes()?.let{strokes ->
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

    private fun showDialog(result: String){
      /*  val dialog = InkResultDialogFragment(result)
        dialog.show(parentFragmentManager, "InkResultDialogFragment")*/

        MaterialAlertDialogBuilder(requireContext(), com.google.android.material.R.style.ThemeOverlay_Material3_MaterialAlertDialog)
            .setTitle("분석 결과")
            .setMessage(result)
            .setPositiveButton("확인") { dialog, _ ->
                dialog.dismiss()
            }
            .show()


    }



}