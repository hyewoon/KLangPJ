package com.hye.sesac.klangpj.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.activityViewModels

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
 * mvp 패턴 -> MLKIt
 */
class DrawFragment : BaseFragment<FragmentDrawBinding>(FragmentDrawBinding::inflate) {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        _binding = FragmentDrawBinding.inflate(inflater, container, false)

        /* viewLifecycleOwner.lifecycleScope.launch {
             viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                 viewModel.uiState.collectLatest {
                   *//*  when {
                        it.isNotEmpty() -> {
                            Toast.makeText(KLangApplication.getKLangContext(),it.toString(),Toast.LENGTH_SHORT).show()
                        }

                        else -> {
                            //  binding.resultText.text = it.message
                            Toast.makeText(KLangApplication.getKLangContext(),"fail",Toast.LENGTH_SHORT).show()

                        }
                    }*//*

                }
            }
        }*/

        return binding.root
    }

    private val viewModel by activityViewModels<HomeViewModel> {
        ViewModelFactory()
    }

    /*private val viewModel by activityViewModels<HomeViewModel> {
        viewModelFactory
    }
*/

    /*   override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
           super.onViewCreated(view, savedInstanceState)

           with(binding) {
               inputBtn.clicks()
                   .onEach {
                       val bitmap = drawingView.getBitmap()
                       viewModel.startTextRecognition(bitmap)
                   }

               eraseBtn.clicks()
                   .onEach {
                       drawingView.clearCanvas()
                   }
                   .launchIn(lifecycleScope)
           }


       }*/
    private fun showDialog() {

    }


}