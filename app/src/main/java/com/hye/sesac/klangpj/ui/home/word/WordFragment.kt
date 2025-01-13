package com.hye.sesac.klangpj.ui.home.word

import android.os.Bundle
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
import com.hye.sesac.domain.firestore.Entity.FirebaseResult
import com.hye.sesac.klangpj.BaseFragment
import com.hye.sesac.klangpj.R
import com.hye.sesac.klangpj.databinding.FragmentWordBinding
import com.hye.sesac.klangpj.ui.factory.ViewModelFactory
import com.hye.sesac.klangpj.ui.viewmodel.HomeViewModel
import kotlinx.coroutines.flow.collectLatest
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


        with(binding) {
            //btn으로 이동
            dictionaryBtn.clicks()
                .onEach {
                    //이중 클릭 방지 코드 넣기
                    navController.navigate(R.id.dictionaryFragment)
                }.launchIn(lifecycleScope)
            writeBtn.clicks()
                .onEach {
                    navController.navigate(R.id.writeDownFragment)
                }.launchIn(lifecycleScope)
            recordBtn.clicks()
                .onEach {
                    navController.navigate(R.id.recordFragment)

                }.launchIn(lifecycleScope)
            listenBtn.clicks()
                .onEach {

                }
            bookmarkBtn.clicks()
                .onEach {
                  //  viewModel.readFireStoreWords()
                }

        }
       // viewLifecycleOwner.lifecycleScope.launch {
       //     viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED){
       //         viewModel.wordContents.collectLatest {
       //             when(it){
       //                 is FirebaseResult.Success ->{
       //                     binding.mainWordTv.text = it.data[0].korean
       //                     binding.wordMeaningTv.text = it.data[0].english
//
        //                }
        //                is FirebaseResult.Failure-> {
        //                    Toast.makeText(requireContext(), it.exception.toString(), Toast.LENGTH_SHORT).show()
//
        //                }
        //                is FirebaseResult.DummyConstructor -> {}
        //                is FirebaseResult.Loading -> {}
        //            }
//
        //        }
        //    }
    //    }


    }
}