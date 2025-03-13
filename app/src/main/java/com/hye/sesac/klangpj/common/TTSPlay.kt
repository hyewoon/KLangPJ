package com.hye.sesac.klangpj.common

import android.content.Context
import android.speech.tts.TextToSpeech
import java.util.Locale

/**
 * 싱글턴으로
 */
object TTSPlay {
    private lateinit var tts: TextToSpeech
    private var isReady = false  // TTS 초기화 완료 상태를 추적

    private fun initTTS(context: Context, onInitComplete: () -> Unit = {}) {
        tts = TextToSpeech(context) { status ->
            isReady = if (status == TextToSpeech.SUCCESS) {
                val result = tts.setLanguage(Locale.KOREA)
                result != TextToSpeech.LANG_MISSING_DATA &&
                        result != TextToSpeech.LANG_NOT_SUPPORTED
            } else false

            if (isReady) {
                tts.setPitch(1.0f)
                tts.setSpeechRate(0.3f)
                onInitComplete()
            }
        }
    }

    fun readText(text: String, context: Context? = null) {
        if (!::tts.isInitialized || !isReady) {
            context?.let { ctx ->
                initTTS(ctx) {
                    // 초기화 완료 후 텍스트 읽기
                    tts.speak(text, TextToSpeech.QUEUE_ADD, null, null)
                }
            } ?: return  // context가 null이면 실행하지 않음
        } else {
            tts.speak(text, TextToSpeech.QUEUE_ADD, null, null)
        }
    }

    fun release() {
        if (::tts.isInitialized) {
            tts.stop()
            tts.shutdown()
            isReady = false
        }
    }
}