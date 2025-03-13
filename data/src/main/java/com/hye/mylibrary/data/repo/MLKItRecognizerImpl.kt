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
import com.hye.domain.model.HandWritingPoint
import com.hye.domain.model.HandWritingStroke
import com.hye.domain.model.MLKitRecognitionResult
import com.hye.domain.repo.MLKitRepository
import com.hye.domain.result.MLKitResult
import com.hye.mylibrary.data.dto.RecognitionResultDto
import com.hye.mylibrary.data.mapper.RecognitionMapper
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await


/**
 * viewModel에서 받은 DataStroke -> Ink 객체로 변환
 */
class MLKItRecognizerImpl() : MLKitRepository {

    private var remoteModelManager = RemoteModelManager.getInstance()
    private var recognizer: DigitalInkRecognizer? = null
    private var model: DigitalInkRecognitionModel? = null
    private var isInitialized = false
    private val mapper: RecognitionMapper = RecognitionMapper()


    companion object MLKitRecognitionFactory {
        fun create() = MLKItRecognizerImpl()
    }


    /**
     * Ink 객체 build & 결과 값 받기
     */


    override suspend fun recognize(strokes: List<HandWritingStroke>) =
        flow<MLKitResult<String>> {
            val ink = convertToInk(strokes)
            val currentRecognizer = recognizer ?: throw Exception("Recognizer not initialized")
            val result = currentRecognizer.recognize(ink).await()

  /*          val bestResult = result.candidates.firstOrNull()?.text ?: "인식 실패"
            val candidate = result.candidates.firstOrNull()?.score as? Number*/



            val bestResult = result.candidates.firstOrNull()?.text ?: "인식 실패"
            val firstCandidate = result.candidates.firstOrNull()

            result.candidates.forEachIndexed { index, candidate ->
                println("Candidate $index: ${candidate.text} with score: ${candidate.score}")
            }

            println("전체 결과: ${result}")
            println("candidates: ${result.candidates}")  // 리스트 확인
            println("첫 번째 후보: $firstCandidate")  // 첫 번째 후보 확인
            println("첫 번째 후보 클래스: ${firstCandidate?.javaClass}")  // 클래스 확인

            val candidate = try {
                firstCandidate?.javaClass?.getDeclaredMethod("getScore")?.invoke(firstCandidate) as? Number
            } catch (e: NoSuchMethodException) {
                println("score 필드 없음")
                null
            }

            println("텍스트: $bestResult")
            println("confidence: ${candidate?.toFloat()}")

            println("텍스트: $bestResult")
            println("confidence: ${candidate?.toFloat()}")



            emit(MLKitResult.Success(bestResult))


     /*       when (bestResult.isNotEmpty()) {
                true -> {

                    println("텍스트 : ${result.candidates.first().text}")
                    println("신뢰도 : ${result.candidates.first().score}")
                    val dto = RecognitionResultDto(
                        recognizedText = result.candidates.first().text ?: "",
                        confidence = result.candidates.first().score!!.toFloat(),
                    )

                    val mapperResult = mapper.mapToDomain(dto)
                    emit(MLKitResult.Success(mapperResult))
                }

                false -> emit( MLKitResult.Failure("인식 오류"))
            }
*/

        }.catch {
            emit(MLKitResult.Failure("인식 오류 ${it.message}"))
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
     * recognizer -> viewModel에서 인식
     */
    suspend fun setUpRecognizer() {
        val modelIdentifier = DigitalInkRecognitionModelIdentifier.fromLanguageTag("ko")
            ?: throw Exception("지원되지 않는 언어입니다.")

        val model = DigitalInkRecognitionModel.builder(modelIdentifier).build()
        downloadModelAndSetUpRecognizer(model)


    }


    /**
     * 모델객체 얻기
     */
    private suspend fun downloadModelAndSetUpRecognizer(model: DigitalInkRecognitionModel) {
        runCatching {

            val currentModel = model ?: throw Exception("Model initialization failed")
            recognizer?.close()
            remoteModelManager.download(currentModel, DownloadConditions.Builder().build())

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

    /**
     * recognizer 정리
     */
    override fun cleanUp() {
        recognizer?.close()
        recognizer = null
        model = null
    }
}
