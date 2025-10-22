package com.hye.sesac.klangpj.ui.home

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import com.hye.domain.usecase.LoadTodayStudyWord
import com.hye.sesac.klangpj.BaseFragment
import com.hye.sesac.klangpj.common.KLangApplication
import com.hye.sesac.klangpj.databinding.FragmentHomeBinding
import com.hye.sesac.klangpj.ui.factory.ViewModelFactory
import com.hye.sesac.klangpj.ui.viewmodel.HomeViewModel
import com.hye.sesac.klangpj.ui.viewmodel.SharedViewModel
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import ru.ldralighieri.corbind.view.clicks


class HomeFragment : BaseFragment<FragmentHomeBinding>(FragmentHomeBinding::inflate) {
    private lateinit var navController: NavController
    private lateinit var dayIcons: List<ImageView>
    private lateinit var useCase: LoadTodayStudyWord
    private val appContainer by lazy {
        (requireActivity().application as KLangApplication).appContainer
    }



    @SuppressLint("SetTextI18n")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)


        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                launch {
                    sharedViewModel.resetDailyWordCount()
                    sharedViewModel.targetWordCount.collect { target ->
                        with(binding) {
                            circularIndicator.max = target
                            todayTarget.text = target.toString()
                        }
                    }
                }
                launch {
                    sharedViewModel.currentWordCount.collect { current ->
                        with(binding) {
                            binding.circularIndicator.progress = current
                            binding.currentWord.text = current.toString()
                            Log.d("HomeFragment", "currentWord: $current")
                        }

                    }
                }
            }
        }

        return binding.root
    }

    private val sharedViewModel: SharedViewModel by activityViewModels {
        ViewModelFactory(
            appContainer = appContainer,
        )
    }
    private val viewModel: HomeViewModel by activityViewModels {
        ViewModelFactory(
            appContainer, sharedViewModel)
        }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navController = findNavController()

        // UseCase 초기화
       // useCase = LoadTodayStudyWord(fireStoreRepository, studyRoomRepository)



        with(binding) {
            mainLearningBtn.clicks()
                .onEach {
                    lifecycleScope.launch {
                        val count = sharedViewModel.targetWordCount.first()
                        val argsActions =
                            HomeFragmentDirections.actionHomeFragmentToWordFragment(count)
                        navController.navigate(argsActions)
                    }
                }.launchIn(lifecycleScope)


        }
    }


}