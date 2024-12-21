package com.hye.sesac.klangpj.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.NavHostFragment
import com.hye.sesac.klangpj.BaseFragment
import com.hye.sesac.klangpj.R
import com.hye.sesac.klangpj.databinding.FragmentSpeechToTextBinding
import kotlinx.coroutines.flow.onEach
import ru.ldralighieri.corbind.material.selections

class SpeechToTextFragment :
    BaseFragment<FragmentSpeechToTextBinding>(FragmentSpeechToTextBinding::inflate) {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        _binding = FragmentSpeechToTextBinding.inflate(inflater, container, false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val navHostFragment =
            childFragmentManager.findFragmentById(R.id.nested_nav_host_fragment) as NavHostFragment
                ?: return
        val navController = navHostFragment.navController


        with(binding) { // Flow<TabLayout.Tab> 반환
            tabLayout.selections()
                .onEach { tab ->
                    when (tab.position) {
                        0 -> navController.navigate(R.id.toSpeechFragment)
                        else -> navController.navigate(R.id.toTextFragment)
                    }
                }
        }


    }

}