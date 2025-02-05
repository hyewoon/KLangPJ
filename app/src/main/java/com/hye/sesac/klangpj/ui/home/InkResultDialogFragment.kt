package com.hye.sesac.klangpj.ui.home

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.DialogFragment

class InkResultDialogFragment(private val recognizedText : String) : DialogFragment() {
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val builder = AlertDialog.Builder(requireContext())
        builder.setTitle("분석한 단어")
            .setMessage(recognizedText)
            .setPositiveButton("확인"){ dialog,_ ->
                dialog.dismiss()
            }
        return builder.create()
    }

}