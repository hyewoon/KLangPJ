package com.hye.sesac.klangpj.ui.mypage

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.hye.sesac.klangpj.BaseFragment
import com.hye.sesac.klangpj.databinding.FragmentMyPageBinding
import com.hye.sesac.klangpj.ui.factory.viewModelFactory
import com.hye.sesac.klangpj.ui.model.MyPageViewModel


class MyPageFragment : BaseFragment<FragmentMyPageBinding>(FragmentMyPageBinding::inflate) {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        _binding = FragmentMyPageBinding.inflate(inflater, container, false)
        return binding.root
    }

    private val viewModel by viewModels<MyPageViewModel>{
        viewModelFactory
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
//       val navHostFragment= childFragmentManager.findFragmentById(R.id.nested_nav_host_fragment) as NavHostFragment ?: return
//       val navController = navHostFragment.navController


        with(binding) {


        }
    }
}



