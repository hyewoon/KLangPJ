package com.hye.sesac.klangpj.ui.game

import android.os.Bundle
import android.speech.RecognitionListener
import android.speech.SpeechRecognizer
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import com.hye.sesac.klangpj.BaseFragment
import com.hye.sesac.klangpj.common.throttleFirst
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import ru.ldralighieri.corbind.view.clicks
import android.Manifest
import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.speech.RecognizerIntent
import android.util.Log
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.repeatOnLifecycle
import com.gun0912.tedpermission.PermissionListener
import com.gun0912.tedpermission.normal.TedPermission
import com.hye.sesac.klangpj.common.showToast
import com.hye.sesac.klangpj.databinding.FragmentSttBinding
import com.hye.sesac.klangpj.ui.viewmodel.GameViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

/**
 * stt
 * 런타임퍼미션 : tedPermission
 */
class STTFragment : BaseFragment<FragmentSttBinding>(FragmentSttBinding::inflate) {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        _binding = FragmentSttBinding.inflate(layoutInflater, container, false)


        return binding.root
    }

    private val viewModel by activityViewModels<GameViewModel>()

    // Launcher 추가
    private val sttLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == android.app.Activity.RESULT_OK) {
            viewModel.handleSTTResult(result.data)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewLifecycleOwner.lifecycleScope.launch {
               repeatOnLifecycle(Lifecycle.State.STARTED){
                   viewModel.sttText.collectLatest {
                       with(binding){
                            resultCardView.visibility = View.VISIBLE
                           binding.resultTv.text = it
                       }
                   }
               }
        }

        binding.speechBtn.clicks()
            .throttleFirst(300L)
            .onEach {
                checkPermission()
            }
            .launchIn(lifecycleScope)
    }

    override fun onStop() {
        super.onStop()
        viewModel.clearSTTText()
    }


    private val permissionListener = object : PermissionListener {
        override fun onPermissionGranted() { //퍼미션 권한 얻으면
            startSTT()
        Log.d("STTFragment", "STT시작")
        }

        override fun onPermissionDenied(deniedPermissions: MutableList<String>?) {
            //거부되면
            showToast("권한을 거부하셨습니다.")

        }

    }
    private fun checkPermission(){
            TedPermission.create()
                .setPermissionListener(permissionListener)
                .setDeniedMessage("음성 인식 위해서는 마이크 권한이 필요합니다.")
                .setPermissions(Manifest.permission.RECORD_AUDIO)
                .check()
    }

    private fun startSTT(){
        try {
            val intent = viewModel.createSTTIntent()
            sttLauncher.launch(intent)
        }catch (e:ActivityNotFoundException){
            showToast("STT를 지원하지 않는 기기입니다.")
        }
    }

  }

