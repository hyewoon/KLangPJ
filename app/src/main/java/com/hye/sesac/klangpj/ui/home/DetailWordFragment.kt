package com.hye.sesac.klangpj.ui.home

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.hye.sesac.klangpj.BaseFragment
import com.hye.sesac.klangpj.R
import com.hye.sesac.klangpj.databinding.FragmentDetailWordBinding
import com.hye.sesac.klangpj.state.TodayWordUiState
import com.hye.sesac.klangpj.ui.factory.ViewModelFactory
import com.hye.sesac.klangpj.ui.viewmodel.HomeViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch


class DetailWordFragment : BaseFragment<FragmentDetailWordBinding>(FragmentDetailWordBinding::inflate) {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentDetailWordBinding.inflate(inflater, container, false)
        return binding.root
    }
    private val viewModel by activityViewModels<HomeViewModel>{
        ViewModelFactory()
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED){
                viewModel.currentWord.collectLatest {
                   it?.let{
                       updateUi(it)
                   }
                }
            }
        }

    }



    private fun updateUi(word:TodayWordUiState){
        with(binding){
            titleTv.text = word.word
            englishTv.text = word.english
            definitionTv.text = word.wordGrade
            examplesTv.text = word.examples.take(6)
                .joinToString ("\n\n") {
                    it
                }
            }

            }

        }




