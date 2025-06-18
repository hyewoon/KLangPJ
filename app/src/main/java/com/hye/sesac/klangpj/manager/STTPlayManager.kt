package com.hye.sesac.klangpj.manager

import android.Manifest
import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.speech.RecognitionListener
import android.speech.RecognizerIntent
import android.speech.SpeechRecognizer
import com.gun0912.tedpermission.PermissionListener
import com.gun0912.tedpermission.normal.TedPermission
import com.hye.sesac.klangpj.common.showToast

class STTPlayManager {
    private lateinit var speechRecognizer: SpeechRecognizer
    private var isListening = false

    private val recognizerIntent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH).apply {
        putExtra(RecognizerIntent.EXTRA_LANGUAGE, "ko-KR")
        putExtra(RecognizerIntent.EXTRA_PARTIAL_RESULTS, true)
    }

    var onResult: ((String) -> Unit)? = null
    var onError: ((String) -> Unit)? = null
    var onListeningStarted: (() -> Unit)? = null
    var onListeningStopped: (() -> Unit)? = null

    fun initSTT(context: Context? = null, onCompleted: () -> Unit = {}) {

        speechRecognizer = SpeechRecognizer.createSpeechRecognizer(context)

        speechRecognizer.setRecognitionListener(object : RecognitionListener {
            override fun onReadyForSpeech(params: Bundle?) {
                isListening = true
                onListeningStarted?.invoke()
            }

            override fun onBeginningOfSpeech() {}

            override fun onRmsChanged(rmsdB: Float) {}

            override fun onBufferReceived(buffer: ByteArray?) {}

            override fun onEndOfSpeech() {
                isListening = false
                onListeningStopped?.invoke()
            }

            override fun onError(error: Int) {
                isListening = false
                onError?.invoke("음성 인식 오류")

            }

            override fun onResults(results: Bundle?) {
                isListening = false
                val text = results?.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION)?.firstOrNull()
                text?.let { onResult?.invoke(it) }
            }

            override fun onPartialResults(partialResults: Bundle?) {}

            override fun onEvent(eventType: Int, params: Bundle?) {}


        })

    }

     fun startListening() {
        if(!isListening){
            speechRecognizer.startListening(recognizerIntent)
        }
    }

    fun stopListening() {
        speechRecognizer.stopListening()
    }

    fun destroy() {
        if (::speechRecognizer.isInitialized) {
            speechRecognizer.destroy()
            isListening = false
        }
    }


}