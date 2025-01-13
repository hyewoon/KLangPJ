package com.hye.sesac.klangpj.ui.mypage

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import com.hye.sesac.klangpj.BaseFragment
import com.hye.sesac.klangpj.R
import com.hye.sesac.klangpj.databinding.FragmentMyPageBinding
import com.hye.sesac.klangpj.ui.factory.ViewModelFactory
import com.hye.sesac.klangpj.ui.viewmodel.MyPageViewModel
import com.hye.sesac.klangpj.ui.viewmodel.RankViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import ru.ldralighieri.corbind.view.clicks


class MyPageFragment : BaseFragment<FragmentMyPageBinding>(FragmentMyPageBinding::inflate) {
    private lateinit var navController: NavController

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        _binding = FragmentMyPageBinding.inflate(inflater, container, false)
        return binding.root
    }

    private val viewModel by viewModels<RankViewModel>{
        ViewModelFactory()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
//       val navHostFragment= childFragmentManager.findFragmentById(R.id.nested_nav_host_fragment) as NavHostFragment ?: return
//       val navController = navHostFragment.navController

       navController = findNavController()
        with(binding) {
            goToLearnBtn.clicks()
                .onEach {
                    navController.navigate(R.id.wordFragment)

                }.launchIn(lifecycleScope)

        }
    }
}



