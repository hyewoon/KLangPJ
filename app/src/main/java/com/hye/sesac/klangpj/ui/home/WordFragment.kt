package com.hye.sesac.klangpj.ui.home

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.hye.domain.usecase.LoadTodayStudyWord
import com.hye.sesac.klangpj.BaseFragment
import com.hye.sesac.klangpj.R
import com.hye.sesac.klangpj.common.KLangApplication.Companion.firestoreRepository
import com.hye.sesac.klangpj.common.KLangApplication.Companion.studyRoomRepository
import com.hye.sesac.klangpj.common.throttleFirst
import com.hye.sesac.klangpj.databinding.FragmentWordBinding
import com.hye.sesac.klangpj.state.TodayWordUiState
import com.hye.sesac.klangpj.ui.factory.ViewModelFactory
import com.hye.sesac.klangpj.ui.viewmodel.HomeViewModel
import com.hye.sesac.klangpj.ui.viewmodel.SharedViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import ru.ldralighieri.corbind.view.clicks

class WordFragment : BaseFragment<FragmentWordBinding>(FragmentWordBinding::inflate) {
    private lateinit var navController: NavController
    private var targetWordCount: Int = 0
    private var url = ""
    private var selected = false
    private val args: WordFragmentArgs by navArgs()
    private lateinit var useCase: LoadTodayStudyWord


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        _binding = FragmentWordBinding.inflate(inflater, container, false)

        return binding.root
    }

    private val sharedViewModel by activityViewModels<SharedViewModel> {
        ViewModelFactory(useCaseProvider = {
            useCase
        }
        )
    }
    private val viewModel by activityViewModels<HomeViewModel> {
        ViewModelFactory { useCase }
    }


    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        useCase = LoadTodayStudyWord(firestoreRepository, studyRoomRepository)

        navController = findNavController()
        targetWordCount = args.targetWordCount

        //오늘의 단어 불러오기
        sendStudyWordNum(targetWordCount)
        Log.d("WordFragment", "onViewCreated: $targetWordCount")

        setupObservers()
        setupClickListeners()
    }

    private fun setupObservers() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                launch {
                    sharedViewModel.targetWordCount.collect{
                        with(binding){
                            todayTargetWordsTv.text = "/$it"
                            linearProgressIndicator.max = it
                        }
                    }
                }
                launch {
                    sharedViewModel.currentWordCount.collect{
                        with(binding){
                            currentWordTv.text = it.toString()
                            linearProgressIndicator.progress = it
                        }
                    }
                }
                launch {
                    viewModel.currentWordsList.collectLatest { word ->
                        word?.let {
                            updateUi(it)
                            url = it.pronunciation
                            updateNavigationButtons(
                                moveNext = viewModel.hasNextWord(),
                                movePrev = viewModel.hasPreviousWord()
                            )
                        }
                    }

                }

            }
        }

    }


    private fun setupClickListeners() {
        with(binding) {
            //btn으로 이동
            dictionaryBtn.clicks()
                .throttleFirst(300L)
                .onEach {
                    //이중 클릭 방지 코드 넣기
                    navController.navigate(R.id.detailWordFragment)
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
                   // showListenDialog(viewModel.currentWord.value!!)
                }.launchIn(lifecycleScope)

            bookmarkBtn.clicks()
                .throttleFirst(300L)
                .onEach {
                    bookmarkBtn.isSelected = true
                    //bookmarkBtn.setImageResource(if (bookmarkBtn.isSelected) R.drawable.checked_star else R.drawable.unchecked_star)
                    bookmarkBtn.setImageResource(R.drawable.checked_star)
                }.launchIn(lifecycleScope)

            nextBtn.clicks()
                .throttleFirst(300L)
                .onEach {
                    viewModel.moveToNextWord()
                }.launchIn(lifecycleScope)

            prevBtn.clicks()
                .throttleFirst(300L)
                .onEach {
                    viewModel.moveToPreviousWord()
                }.launchIn(lifecycleScope)
        }

    }


    private fun sendStudyWordNum(count: Int) {
        viewModel.searchUseCase(count)
    }

    private fun updateUi(state: TodayWordUiState) {
        with(binding) {
            mainWordTv.text = state.word
            wordMeaningTv.text = state.english
            state.examples.forEach {
                exampleSentenceKorean.text = it
            }
        }
    }

    private fun updateNavigationButtons(moveNext: Boolean, movePrev: Boolean) {
        binding.nextBtn.isEnabled = moveNext
        binding.prevBtn.isEnabled = movePrev
    }

}
