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
import androidx.recyclerview.widget.LinearLayoutManager
import com.hye.domain.result.ApiResult
import com.hye.sesac.klangpj.BaseFragment
import com.hye.sesac.klangpj.adapter.WordInfoAdapter
import com.hye.sesac.klangpj.common.KLangApplication
import com.hye.sesac.klangpj.databinding.FragmentDictionaryBinding
import com.hye.sesac.klangpj.ui.factory.ViewModelFactory
import com.hye.sesac.klangpj.ui.viewmodel.GameViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch


class DictionaryFragment :
    BaseFragment<FragmentDictionaryBinding>(FragmentDictionaryBinding::inflate) {
    private val appContainer by lazy {
        (requireActivity().application as KLangApplication).appContainer
    }

    private val viewModel by activityViewModels<GameViewModel> {
        ViewModelFactory(appContainer)
    }
    private lateinit var navController: NavController
    private lateinit var wordInfoAdapter: WordInfoAdapter


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        _binding = FragmentDictionaryBinding.inflate(layoutInflater, container, false)

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.wordInfo.collectLatest {
                    when(it) {
                        is ApiResult.Success -> {
                            wordInfoAdapter.submitList(it.data)
                        }
                        is ApiResult.Failure -> {
                            Log.d("DictionaryFragment",it.message)
                        }
                        else ->{}
                    }


                }
            }
        }

        return binding.root
    }



    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        navController = findNavController()

        with(binding) {
            inputTextLayout.setEndIconOnClickListener {
                val inputWord = binding.editTv.text.toString()
                if (inputWord.isNotEmpty()) {
                    viewModel.getWordInfo(inputWord)
                } else {

                }
            }


            //어뎁터 초기화
            wordInfoAdapter = WordInfoAdapter(
                callBack = { code, trans, dfn ->
                val argsAction =
                    DictionaryFragmentDirections.actionDictionaryFragmentToDetailDictionaryFragment(
                        targetCode = code,
                        transWord = trans,
                        transDfn = dfn
                    )
                navController.navigate(argsAction)
            },

                viewModel = viewModel
            )

            dictionaryRecyclerview.apply {
                layoutManager = LinearLayoutManager(KLangApplication.getKLangContext())
                adapter = wordInfoAdapter

            }

        }
    }

    private fun performSearch(query: String) {
        viewModel.getWordInfo(query)
    }

    override fun onStop() {
        super.onStop()
        binding.editTv.setText("")
        viewModel.wordInfoClear()

    }


}



