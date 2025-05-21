package com.hye.sesac.klangpj.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.hye.sesac.klangpj.BaseFragment
import com.hye.sesac.klangpj.common.throttleFirst
import com.hye.sesac.klangpj.databinding.FragmentWriteDownBinding
import com.hye.sesac.klangpj.state.UiStateResult
import com.hye.sesac.klangpj.ui.factory.ViewModelFactory
import com.hye.sesac.klangpj.ui.viewmodel.HomeViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import ru.ldralighieri.corbind.view.clicks


class WriteDownFragment :
    BaseFragment<FragmentWriteDownBinding>(FragmentWriteDownBinding::inflate) {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        _binding = FragmentWriteDownBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    private val viewModel by activityViewModels<HomeViewModel> {
        ViewModelFactory()
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.currentWord.collectLatest {
                    when (it) {
                        is UiStateResult.Loading -> {
                            //showProgressBar
                        }

                        is UiStateResult.Success -> {
                            binding.drawingView.setWatermarkText(it.data.word)
                        }

                        is UiStateResult.NetWorkFailure -> {
                        }

                        is UiStateResult.RoomDBFailure -> {}
                    }
                }
            }

            binding.refreshBtn.clicks()
                .throttleFirst(300L)
                .onEach {
                    binding.drawingView.clearCanvas()
                }
                .launchIn(lifecycleScope)


        }
    }

}