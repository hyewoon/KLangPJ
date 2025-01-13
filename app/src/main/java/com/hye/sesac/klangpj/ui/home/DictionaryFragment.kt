package com.hye.sesac.klangpj.ui.home

import android.os.Bundle

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
import com.hye.sesac.data.entity.WordInfo

import com.hye.sesac.klangpj.BaseFragment
import com.hye.sesac.klangpj.adapter.WordInfoAdapter
import com.hye.sesac.klangpj.common.KLangApplication

import com.hye.sesac.klangpj.databinding.FragmentDictionaryBinding
import com.hye.sesac.klangpj.ui.factory.ViewModelFactory
import com.hye.sesac.klangpj.ui.viewmodel.HomeViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch


class DictionaryFragment :
    BaseFragment<FragmentDictionaryBinding>(FragmentDictionaryBinding::inflate) {
    private val viewModel by activityViewModels<HomeViewModel> {
        ViewModelFactory()
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
                    when {
                        it.isNotEmpty() -> {
                            wordInfoAdapter.submitList(it)
                        }

                        else -> {
                            emptyList<WordInfo>()
                        }
                    }


                }
            }
        }

        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        with(binding) {
            inputTextLayout.setEndIconOnClickListener {
                val inputWord = binding.editTv.text.toString()
                viewModel.getWordInfo(inputWord)

            }



            navController = findNavController()

            //어뎁터 초기화
            wordInfoAdapter = WordInfoAdapter() {
                val argsAction =
                    DictionaryFragmentDirections.actionDictionaryFragmentToDetailDictionaryFragment(
                        it
                    )
                navController.navigate(argsAction)
            }

            binding.dictionaryRecyclerview.apply {
                layoutManager = LinearLayoutManager(KLangApplication.getKLangContext())
                adapter = wordInfoAdapter

            }


            /*

    private fun displayWordList() {
     navController = findNavController()
        with(binding) {
            dictionaryRecyclerview.layoutManager =
                LinearLayoutManager(KLangApplication.getKLangContext())

            dictionaryRecyclerview.adapter = WordInfoAdapter() {
                val argsAction =
                    DictionaryFragmentDirections.actionDictionaryFragmentToDetailDictionaryFragment(
                       it
                    )
                navController.navigate(argsAction)
            }.apply {
            replaceWordInfo(wordInfo)
        }
        }*/

        }
    }
}



