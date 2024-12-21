package com.hye.sesac.klangpj.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import com.hye.sesac.klangpj.BaseFragment
import com.hye.sesac.klangpj.R
import com.hye.sesac.klangpj.databinding.FragmentHomeBinding
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import ru.ldralighieri.corbind.view.clicks


class HomeFragment : BaseFragment<FragmentHomeBinding>(FragmentHomeBinding::inflate) {
    private lateinit var navController: NavController

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navController = findNavController()

        with(binding) {
            mainCardView.clicks()
                .onEach {
                    navController.navigate(R.id.wordFragment)
                }.launchIn(viewLifecycleOwner.lifecycleScope)

            speechToTextCardView.clicks()
                .onEach {
                    navController.navigate(R.id.speechToTextFragment)

                }.launchIn(viewLifecycleOwner.lifecycleScope)
            drawCardView.clicks()
                .onEach {
                    navController.navigate(R.id.writeFragment)
                }.launchIn(viewLifecycleOwner.lifecycleScope)
            searchCardView.clicks()
                .onEach {
                    navController.navigate(R.id.dictionaryFragment)
                }.launchIn(viewLifecycleOwner.lifecycleScope)

        }
    }

}