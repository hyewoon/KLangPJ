package com.hye.sesac.klangpj.ui.home.word

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import com.hye.domain.model.TargetWordEntity
import com.hye.domain.result.FirebaseResult
import com.hye.sesac.klangpj.BaseFragment
import com.hye.sesac.klangpj.MainActivity
import com.hye.sesac.klangpj.R
import com.hye.sesac.klangpj.common.throttleFirst
import com.hye.sesac.klangpj.databinding.FragmentWordBinding
import com.hye.sesac.klangpj.state.TodayWordUiState
import com.hye.sesac.klangpj.state.UiStateResult
import com.hye.sesac.klangpj.ui.factory.ViewModelFactory
import com.hye.sesac.klangpj.ui.viewmodel.HomeViewModel
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import ru.ldralighieri.corbind.view.clicks

class WordFragment : BaseFragment<FragmentWordBinding>(FragmentWordBinding::inflate) {
    private lateinit var navController: NavController


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        _binding = FragmentWordBinding.inflate(inflater, container, false)


        return binding.root
    }

    private val viewModel by activityViewModels<HomeViewModel> {
        ViewModelFactory()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        navController = findNavController()

        binding.linearProgressIndicator.progress = 4

        if (viewModel.todayWordUiState.value is UiStateResult.Loading) {
            sendStudyWordNum(10)  // 처음 실행될 때만 호출
        }

        setupObservers()
        setupClickListeners()
    }


    private fun setupObservers() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                combine(
                    viewModel.todayWordUiState,
                    viewModel.currentIndex
                ) { state, index ->
                    Pair(state, index)
                }.collect { (state, index) ->
                    Log.d("WordFragment", "state: $state, index: $index")
                    when (state) {
                        is UiStateResult.Loading -> {
                        }
                        is UiStateResult.Success -> {
                            state.data.getOrNull(index)?.let { word ->
                                updateUi(word)
                                updateNavigationButtons(
                                    canMoveNext = viewModel.hasNextWord()
                                )
                            }
                        }
                        is UiStateResult.NetWorkFailure -> {
                        }
                        is UiStateResult.RoomDBFailure -> {
                        }
                    }
                }
            }
        }
    }

    private fun setupClickListeners(){

        with(binding) {
            //btn으로 이동
            dictionaryBtn.clicks()
                .throttleFirst(300L)
                .onEach {
                    //이중 클릭 방지 코드 넣기
                    navController.navigate(R.id.dictionaryFragment)
                }.launchIn(lifecycleScope)
            writeBtn.clicks()
                .throttleFirst(300L)
                .onEach {
                    navController.navigate(R.id.writeDownFragment)
                }.launchIn(lifecycleScope)
            recordBtn.clicks()
                .throttleFirst(300L)
                .onEach {
                    navController.navigate(R.id.recordFragment)

                }.launchIn(lifecycleScope)
            listenBtn.clicks()
                .throttleFirst(300L)
                .onEach {

                }.launchIn(lifecycleScope)
            bookmarkBtn.clicks()
                .throttleFirst(300L)
                .onEach {
                }.launchIn(lifecycleScope)


            nextBtn.clicks()
                .throttleFirst(300L)
                .onEach {
                    viewModel.moveToNextWord()
                    Log.d("Word", "clicke됨")

                }.launchIn(lifecycleScope)

        }

    }

    private fun sendStudyWordNum(count: Long) {
        viewModel.getFireStoreInfo(count)
    }

    private fun updateUi(state: TodayWordUiState){
        with(binding){
            mainWordTv.text =state.word
            wordMeaningTv.text = state.wordGrade
            state.examples.forEach {
                exampleSentenceKorean.text = it
            }

            }

        }


    private fun updateNavigationButtons(canMoveNext: Boolean) {
        binding.nextBtn.isEnabled = canMoveNext
    }

}
