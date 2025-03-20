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
import android.content.Intent
import android.speech.RecognizerIntent
import android.util.Log
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import com.gun0912.tedpermission.PermissionListener
import com.gun0912.tedpermission.normal.TedPermission
import com.hye.sesac.klangpj.common.showToast
import com.hye.sesac.klangpj.databinding.FragmentSttBinding

/**
 * stt
 * 런타임퍼미션 : tedPermission
 */
class STTFragment : BaseFragment<FragmentSttBinding>(FragmentSttBinding::inflate) {


    private val resultLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == android.app.Activity.RESULT_OK) {
            val spokenText = result.data?.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS)?.get(0) ?: ""
            binding.resultCardView.visibility = View.VISIBLE
            binding.resultTv.text = spokenText
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        _binding = FragmentSttBinding.inflate(layoutInflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.speechBtn.clicks()
            .throttleFirst(300L)
            .onEach {
                checkPermission()
            }
            .launchIn(lifecycleScope)
    }
    private val permissionListener = object : PermissionListener {
        override fun onPermissionGranted() { //퍼미션 권한 얻으면
            initSTT()
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

    private fun initSTT() {
        val intent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH).apply {
            putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM)
            putExtra(RecognizerIntent.EXTRA_LANGUAGE, "ko-KR")
        }

        try {
            resultLauncher.launch(intent)
        } catch (e: ActivityNotFoundException) {

            showToast("음성인식을 지원하지 않는 기기입니다.")
        }
    }




}