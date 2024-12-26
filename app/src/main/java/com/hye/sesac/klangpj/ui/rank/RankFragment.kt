package com.hye.sesac.klangpj.ui.rank

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.hye.sesac.klangpj.BaseFragment
import com.hye.sesac.klangpj.R
import com.hye.sesac.klangpj.adapter.RankAdapter
import com.hye.sesac.klangpj.common.KLangApplication
import com.hye.sesac.klangpj.databinding.FragmentRankBinding
import com.hye.sesac.klangpj.ui.factory.viewModelFactory
import com.hye.sesac.klangpj.ui.model.RankViewModel


class RankFragment : BaseFragment<FragmentRankBinding>(FragmentRankBinding::inflate) {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        _binding = FragmentRankBinding.inflate(inflater, container, false)
        return binding.root
    }
    private val viewModel by viewModels<RankViewModel>{
        viewModelFactory
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        with(binding) {
            recyclerView.layoutManager = LinearLayoutManager(
                KLangApplication.getKLangContext(),
                LinearLayoutManager.VERTICAL,
                false
            )
            val listAdapter = RankAdapter { imageId, name ->

            }
            recyclerView.adapter = listAdapter


        }
    }
}

