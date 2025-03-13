package com.hye.sesac.klangpj.common

import android.content.Context
import android.speech.tts.TextToSpeech
import android.util.Log
import android.view.Gravity
import android.widget.TextView
import android.widget.Toast
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import java.util.Locale

fun showDialog(result: String, context: Context) {
    val messageTextView = TextView(context).apply {
        text = result
        textSize = 25f
        gravity = Gravity.CENTER

    }

    MaterialAlertDialogBuilder(
        context,
        com.google.android.material.R.style.ThemeOverlay_Material3_MaterialAlertDialog
    )
        .setTitle("분석 결과")
        .setView(messageTextView)
        .setPositiveButton("확인") { dialog, _ ->
            dialog.dismiss()
        }
        .show()
}


fun showToast(message: String, delayTime: Int = Toast.LENGTH_SHORT){
    Toast.makeText(KLangApplication.getKLangContext(), message, delayTime).show()
}


