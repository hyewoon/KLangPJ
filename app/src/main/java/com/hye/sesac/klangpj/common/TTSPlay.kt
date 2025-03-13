package com.hye.sesac.klangpj.common

import android.content.Context
import android.speech.tts.TextToSpeech
import java.util.Locale

object TTSPlay {
    private lateinit var tts: TextToSpeech
    private fun initTTS(context: Context){
        tts = TextToSpeech(context) { status ->
            if (status == TextToSpeech.SUCCESS) {
                val result = tts.setLanguage(Locale.KOREA)
                if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                    return@TextToSpeech
                }
            }

        }
    }
    fun readText(text: String) {
        if (!::tts.isInitialized) {
            initTTS(KLangApplication.getKLangContext())
        }
        with(tts) {
            speak(text, TextToSpeech.QUEUE_ADD, null, null)
            setPitch(1.0f)//높낮이 0.5f ~ 2.0f
            setSpeechRate(0.3f) //속도 0.1f ~10.0f
        }
    }
}