package com.hye.sesac.klangpj.ui.home

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
import androidx.navigation.fragment.navArgs
import com.hye.sesac.klangpj.BaseFragment
import com.hye.sesac.klangpj.databinding.FragmentDetailDictionaryBinding
import com.hye.sesac.klangpj.ui.factory.ViewModelFactory
import com.hye.sesac.klangpj.ui.viewmodel.HomeViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch


class DetailDictionaryFragment :
    BaseFragment<FragmentDetailDictionaryBinding>(FragmentDetailDictionaryBinding::inflate) {
    private lateinit var navController: NavController
    private val args: DetailDictionaryFragmentArgs by navArgs()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentDetailDictionaryBinding.inflate(layoutInflater, container, false)

        //UI update
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.detailWordInfo.collectLatest {
                    binding.detailTv.text = it.toString()
                }
            }
        }
        return binding.root

    }

    private val viewModel by activityViewModels<HomeViewModel> {
        ViewModelFactory()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navController = findNavController()
        val target = args.targetCode.toString()
        Toast.makeText(requireContext(), target.toString(), Toast.LENGTH_SHORT).show()
        //Log.d(TAG, target.toString())

        getDetailWordInfo(target)

    }

    //타겟 코드 보내고
    private fun getDetailWordInfo(targetCode: String) {
        viewModel.getDetailWordInfo(targetCode)

    }


}