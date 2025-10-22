package com.hye.sesac.klangpj.ui.game

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.hye.domain.result.ApiResult
import com.hye.sesac.klangpj.BaseFragment
import com.hye.sesac.klangpj.common.KLangApplication
import com.hye.sesac.klangpj.databinding.FragmentDetailDictionaryBinding
import com.hye.sesac.klangpj.ui.factory.ViewModelFactory
import com.hye.sesac.klangpj.ui.viewmodel.GameViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch


class DetailDictionaryFragment :
    BaseFragment<FragmentDetailDictionaryBinding>(FragmentDetailDictionaryBinding::inflate) {
    private lateinit var navController: NavController
    private val args: DetailDictionaryFragmentArgs by navArgs()
    private var transition: String = ""
    private var transDfn: String = ""
    private val appContainer by lazy {
        (requireActivity().application as KLangApplication).appContainer
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentDetailDictionaryBinding.inflate(layoutInflater, container, false)

        //UI update
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.detailWordInfo.collectLatest { it ->
                    when (it) {
                        is ApiResult.Success -> {
                            val result = it.data.forEach { item ->
                                binding.titleTv.text = item.word
                                binding.subTitleTv.text = transition
                                    .removeSurrounding("[", "]")
                                    .split("; ")
                                    .take(3)
                                    .joinToString(", ")


                                val examples = item.examples
                                    .take(6)
                                    .joinToString("\n\n") { example ->
                                        "${example.example}"

                                    }
                                binding.examplesTv.text = examples
                                binding.definitionTv.text = transDfn.removeSurrounding("[", "]")


                            }

                        }

                        is ApiResult.Failure -> Log.d("error", it.message)
                        is ApiResult.Loading -> {}
                        is ApiResult.DummyConstructor -> {}
                    }
                }
            }
        }
        return binding.root

    }

    private val viewModel by activityViewModels<GameViewModel> {
        ViewModelFactory(appContainer)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navController = findNavController()
        val target = args.targetCode.toString()
        transition = args.transWord


    transDfn = args.transDfn

    getDetailWordInfo(target)
}



    //타겟 코드 보내고
    private fun getDetailWordInfo(targetCode: String) {
        viewModel.getDetailWordInfo(targetCode)

    }


}