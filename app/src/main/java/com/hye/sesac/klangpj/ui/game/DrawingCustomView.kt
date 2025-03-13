package com.hye.sesac.klangpj.ui.home

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
 * AttributeSet : xml에서 view를 inflate 할때 필요한 클래스

 */
class DrawingCustomView(context: Context, attrs: AttributeSet) : View(context, attrs) {
    /**
     * 1. onDraw()
     * Canvas -> what to draw : 라인, 텍스트, 사각형
     * Paint -> how to draw : 라인의 색상, 폰트...
     */
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


    private val currentPoints = mutableListOf<HandWritingPoint>()
    private val strokes = mutableListOf<HandWritingStroke>()


    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        canvas.drawPath(path, paint)
    }


    /**
     * 터치이벤트 처리
     */
    override fun onTouchEvent(event: MotionEvent): Boolean {
        val x = event.x
        val y = event.y

        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                currentPoints.clear()
                path.moveTo(x, y)
                addPoint(event)

            }

            MotionEvent.ACTION_MOVE -> {
                path.lineTo(x, y)
                addPoint(event)

            }

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