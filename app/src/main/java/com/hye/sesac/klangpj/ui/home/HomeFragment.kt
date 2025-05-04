package com.hye.sesac.klangpj.ui.home

import android.os.Bundle
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
import com.hye.sesac.klangpj.common.KLangApplication.Companion.firestoreRepository
import com.hye.sesac.klangpj.common.KLangApplication.Companion.studyRoomRepository
import com.hye.sesac.klangpj.databinding.FragmentHomeBinding
import com.hye.sesac.klangpj.ui.factory.ViewModelFactory
import com.hye.sesac.klangpj.ui.viewmodel.HomeViewModel
import com.hye.sesac.klangpj.ui.viewmodel.SharedViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import ru.ldralighieri.corbind.view.clicks


class HomeFragment : BaseFragment<FragmentHomeBinding>(FragmentHomeBinding::inflate) {
    private lateinit var navController: NavController
    private lateinit var dayIcons : List<ImageView>
    private lateinit var useCase : LoadTodayStudyWord


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)


        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED){
                sharedViewModel.targetWordCount.collectLatest { target->
                    binding.circularIndicator.max = target
                    binding.todayTarget.text = target.toString()

                }
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED){
                sharedViewModel.currentWordCount.collectLatest { current->
                    binding.circularIndicator.progress = current
                    binding.currentWord.text = (current).toString()

                }
            }

        }

        return binding.root
    }

    private val sharedViewModel by activityViewModels<SharedViewModel>{
        ViewModelFactory(useCaseProvider = {
            useCase
        }

        )
    }
    private val viewModel by activityViewModels<HomeViewModel>{
       ViewModelFactory {
           useCase }
    }




    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navController = findNavController()

        // UseCase 초기화
        useCase= LoadTodayStudyWord(firestoreRepository, studyRoomRepository)



        with(binding) {

        dayIcons = listOf(
           checkboxSun, checkboxMon,checkboxTue, checkboxWed, checkboxThu, checkboxFri, checkboxSat
        )


            //navigation
            mainLearningBtn.clicks()
                .onEach {
                    lifecycleScope.launch {
                        val count = sharedViewModel.getTargetWordCount()
                        val argsActions =
                            HomeFragmentDirections.actionHomeFragmentToWordFragment(count)
                        navController.navigate(argsActions)

                    }
                }.launchIn(lifecycleScope)


        }
    }


}