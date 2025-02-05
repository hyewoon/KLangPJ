package com.hye.mylibrary.data.repo

import com.google.mlkit.common.model.DownloadConditions
import com.google.mlkit.common.model.RemoteModelManager
import com.google.mlkit.vision.digitalink.DigitalInkRecognition
import com.google.mlkit.vision.digitalink.DigitalInkRecognitionModel
import com.google.mlkit.vision.digitalink.DigitalInkRecognitionModelIdentifier
import com.google.mlkit.vision.digitalink.DigitalInkRecognizer
import com.google.mlkit.vision.digitalink.DigitalInkRecognizerOptions
import com.google.mlkit.vision.digitalink.Ink
import com.google.mlkit.vision.digitalink.Ink.Point
import com.google.mlkit.vision.digitalink.Ink.Stroke
import com.hye.domain.data.HandWritingPoint
import com.hye.domain.data.HandWritingStroke
import com.hye.domain.data.MLKitResult
import com.hye.domain.repo.MLKitHandwritingRecognizer
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext


/**
 * viewModel에서 받은 DataStroke -> Ink 객체로 변환
 */
class MLKItRecognizerImpl : MLKitHandwritingRecognizer {
    private var remoteModelManager = RemoteModelManager.getInstance()
    private var recognizer: DigitalInkRecognizer? = null
    private var model: DigitalInkRecognitionModel? = null
    private var isInitialized = false

    companion object MLKitRecognitionFactory {
        fun create() = MLKItRecognizerImpl()
    }


    /**
     * Ink 객체 build & 결과 값 받기
     */
    override suspend fun recognize(strokes: List<HandWritingStroke>): MLKitResult<String> {
        return runCatching {
            withContext(Dispatchers.IO) {

                if (!isInitialized) {
                    setUpRecognizer()
                }
                val ink = convertToInk(strokes)
                recognizeInk(ink)
            }
        }.fold(onSuccess = { MLKitResult.Success(it) }, onFailure = { e ->
            e.printStackTrace()
            MLKitResult.Failure(e.toString())
        })
    }



    private fun convertToInk(strokes: List<HandWritingStroke>): Ink {
        return Ink.builder().apply {
            strokes.forEach { stroke ->
                addStroke(convertToStroke(stroke))
            }
        }.build()
    }

    private fun convertToStroke(stroke: HandWritingStroke): Stroke {
        return Stroke.builder().apply {
            stroke.pointData.forEach { point ->
                addPoint(convertToPoint(point))

            }
        }.build()
    }

    private fun convertToPoint(point: HandWritingPoint): Point {
        return Point.create(
            point.x, point.y, point.timestamp
        )

    }


    /**
     * recognizer
     */
    private suspend fun setUpRecognizer() {
        val modelIdentifier = DigitalInkRecognitionModelIdentifier.fromLanguageTag("ko")
            ?: throw Exception("지원되지 않는 언어입니다.")

        val model = DigitalInkRecognitionModel.builder(modelIdentifier).build()
        downloadModelAndSetUpRecognizer(model)

    }


    /**
     * 모델객체 얻기
     */
    private suspend fun downloadModelAndSetUpRecognizer(model: DigitalInkRecognitionModel) {
        withContext(Dispatchers.IO) {
            runCatching {

                val currentModel = model ?: throw Exception("Model initialization failed")
                recognizer?.close()
                remoteModelManager.download(currentModel, DownloadConditions.Builder().build()).await()
                recognizer = DigitalInkRecognition.getClient(
                    DigitalInkRecognizerOptions.builder(currentModel).build()
                )
                isInitialized = true


            }.onFailure {
                it.printStackTrace()
                isInitialized = false
                recognizer = null


            }
        }

    }
   private suspend fun recognizeInk(ink: Ink): String {
        return withContext(Dispatchers.IO) {
            val currentRecognizer = recognizer ?: throw Exception("Recognizer not initialized")
            val result = currentRecognizer.recognize(ink).await()
            result.candidates.firstOrNull()?.text ?: throw Exception("No recognition result")

        }
    }

    override fun cleanUp() {
        recognizer?.close()
        recognizer = null
        model = null
    }


}
