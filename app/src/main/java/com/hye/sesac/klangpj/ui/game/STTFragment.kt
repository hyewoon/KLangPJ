package com.hye.sesac.klangpj.ui.home

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
import android.util.Log
import android.widget.Toast
import com.gun0912.tedpermission.PermissionListener
import com.gun0912.tedpermission.normal.TedPermission
import com.hye.sesac.klangpj.databinding.FragmentSttBinding

/**
 * stt
 * 런타임퍼미션 : tedPermission
 */
class STTFragment : BaseFragment<FragmentSttBinding>(FragmentSttBinding::inflate) {
    private var isRecorded: Boolean = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        _binding = FragmentSttBinding.inflate(layoutInflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.micBtn.clicks()
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
            Toast.makeText(requireContext(), "권한을 거부 하셨습니다.", Toast.LENGTH_SHORT).show()
        }

    }
    private fun checkPermission(){
            TedPermission.create()
                .setPermissionListener(permissionListener)
                .setDeniedMessage("음성 인식 위해서는 마이크 권한이 필요합니다.")
                .setPermissions(Manifest.permission.RECORD_AUDIO)
                .check()
    }


  private fun initSTT(){
      SpeechRecognizer.createSpeechRecognizer(requireContext()).also{
         object : RecognitionListener{
             override fun onReadyForSpeech(params: Bundle?) {
                //음성 인식 준비가 되면 ui 상태 변경
             }

             override fun onBeginningOfSpeech() {

             }

             override fun onRmsChanged(rmsdB: Float) {

             }

             override fun onBufferReceived(buffer: ByteArray?) {

             }

             override fun onEndOfSpeech() {

             }
            //에러처리
             override fun onError(error: Int) {
                Log.d("STT", error.toString())
             }

             //음성 인식 결과 처리
             override fun onResults(results: Bundle?) {
                 results?.let{
                     it.getStringArrayList(
                         SpeechRecognizer.RESULTS_RECOGNITION
                     )

                     with(binding.resultTv){
                         binding.micBtn.visibility = View.GONE
                         visibility = View.VISIBLE
                         text = it.toString()
                     }

                 }
             }

             override fun onPartialResults(partialResults: Bundle?) {

             }

             override fun onEvent(eventType: Int, params: Bundle?) {

             }

         }
     }




  }


}