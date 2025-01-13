package com.hye.sesac.klangpj.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import com.hye.sesac.klangpj.BaseFragment
import com.hye.sesac.klangpj.R
import com.hye.sesac.klangpj.databinding.FragmentHomeBinding
import com.hye.sesac.klangpj.ui.factory.ViewModelFactory
import com.hye.sesac.klangpj.ui.viewmodel.HomeViewModel
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
    private val viewModel by activityViewModels<HomeViewModel>{
       ViewModelFactory()
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navController = findNavController()

        with(binding) {

            circularIndicator.apply {
                max = 10
                progress= 4
            }
            currentWord.text = circularIndicator.progress.toString()


            //navigation
            mainCardView.clicks()
                .onEach {
                    navController.navigate(R.id.wordFragment)
                }.launchIn(lifecycleScope)

            speechToTextCardView.clicks()
                .onEach {
                    navController.navigate(R.id.speechToTextFragment)

                }.launchIn(lifecycleScope)
            drawCardView.clicks()
                .onEach {
                    navController.navigate(R.id.drawFragment)
                }.launchIn(lifecycleScope)
            searchCardView.clicks()
                .onEach {
                    navController.navigate(R.id.dictionaryFragment)
                }.launchIn(lifecycleScope)
            myDictionaryBtn.clicks()
                .onEach {
                   // navController.navigate(R.id.myVocaFragment)
                }


        }
    }

}