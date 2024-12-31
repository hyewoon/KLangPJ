package com.hye.sesac.klangpj.common

import android.app.Application
import com.google.firebase.FirebaseApp
import com.hye.sesac.klangpj.R
import org.apache.poi.xssf.usermodel.XSSFBuiltinTableStyle.init
import java.io.File

class KLangApplication : Application() {

    init {

        val stream = resources.openRawResource(R.raw.vision_key)
        val credentialsPath = File(filesDir, "vision_credentials.json")
        credentialsPath.writeBytes(stream.readBytes())

        System.setProperty(
            "GOOGLE_APPLICATION_CREDENTIALS",
            credentialsPath.absolutePath
        )
    }
    override fun onCreate() {
        super.onCreate()
        appInstance = this
        FirebaseApp.initializeApp(this)

    }

    companion object {
        lateinit var appInstance: KLangApplication
        fun getKLangContext(): KLangApplication = appInstance
    }
}