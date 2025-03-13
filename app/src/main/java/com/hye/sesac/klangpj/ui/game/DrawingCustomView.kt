package com.hye.sesac.klangpj.ui.game

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import android.view.View
import com.hye.domain.model.HandWritingPoint
import com.hye.domain.model.HandWritingStroke


/**
 * AttributeSet : xml에서 view를 inflate 할때 필요한 클래스로
 *  --> height, width 등 정하는 속성들이 attributeSet에 지정된다. --> setContentsView()
 style 속성은 3번째 속성에 지정
 */
class DrawingCustomView(context: Context, attrs: AttributeSet) : View(context, attrs) {
    /**
     * 1. onDraw()
     * Canvas -> what to draw : 라인, 텍스트, 사각형
     * Paint -> how to draw : 라인의 색상, 폰트...
     */
    private var originalWidth: Float = 0f
    private var originalHeight: Float = 0f

    private val path = android.graphics.Path()
    private val paint = Paint().apply {
        //line 크기 및 컬러 설정
        color = Color.BLACK
        style = Paint.Style.STROKE
        strokeJoin = Paint.Join.ROUND
        strokeCap = Paint.Cap.ROUND
        strokeWidth = 10f
        isAntiAlias = true
    }


    // 워터마크용 Paint 추가
    private val watermarkPaint = Paint().apply {
        color = Color.GRAY
        alpha = 128  // 투명도
        textSize = 360f
        textAlign = Paint.Align.CENTER
    }

    private var watermarkText = ""


    // 워터마크 설정 메서드
    fun setWatermarkText(text: String) {
        watermarkText = text
        invalidate() // 화면 갱신
    }


    private val currentPoints = mutableListOf<HandWritingPoint>()
    private val strokes = mutableListOf<HandWritingStroke>()


    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)


        canvas.save()
        // 현재 뷰의 크기에 맞게 콘텐츠를 스케일링하기 위한 로직
        // 원본 크기를 저장해야 함 (클래스 변수로 추가)
        if (originalWidth == 0f || originalHeight == 0f) {
            originalWidth = width.toFloat()
            originalHeight = height.toFloat()
        } else if (width > 0 && height > 0) {
            // 스케일 계수 계산
            val scaleX = width.toFloat() / originalWidth
            val scaleY = height.toFloat() / originalHeight

            // Canvas에 스케일 적용
            canvas.scale(scaleX, scaleY)
        }



        // 워터마크 그리기
        canvas.drawText(watermarkText, width/2f, height/2f, watermarkPaint)

        canvas.drawPath(path, paint)

    }


    /**
     * 터치이벤트 처리
     */
    override fun onTouchEvent(event: MotionEvent): Boolean {
        val x = event.x
        val y = event.y

        when (event.action) {
            //터치 시작
            //터치 시작시간에서 up까지의 시간
            MotionEvent.ACTION_DOWN -> {
                currentPoints.clear()
                path.moveTo(x, y)
                addPoint(event)

            }

            MotionEvent.ACTION_MOVE -> {
                path.lineTo(x, y)
                addPoint(event)

            }
            //터치 끝남
            MotionEvent.ACTION_UP -> {
                path.lineTo(x, y)
                addPoint(event)
                finishStroke()


            }
        }
        invalidate()
        return true
    }

    override fun performClick(): Boolean {
        super.performClick()
        return true
    }


    fun clearCanvas() {
        currentPoints.clear()
        strokes.clear()
        path.reset()
        invalidate()
    }

    private fun addPoint(event: MotionEvent) {
        currentPoints.add(
            HandWritingPoint(
                x = event.x,
                y = event.y,
                timestamp = System.currentTimeMillis()
            )
        )
    }

    fun getCurrentStrokes(): List<HandWritingStroke> =
        if (strokes.isNotEmpty()) strokes.toList()
        else emptyList()


    private fun finishStroke() {
        if (currentPoints.isNotEmpty()) {
            strokes.add(HandWritingStroke(currentPoints.toList()))
            currentPoints.clear()
            Log.e("View", "Stroke completed: ${strokes.size} strokes total")
        }
    }


}