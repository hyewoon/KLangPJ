package com.hye.sesac.klangpj.ui.home

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View

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
        strokeWidth = 12f
        isAntiAlias = true

    }


    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        canvas.apply {
            drawPath(path, paint)
        }

    }

    private var lastX = 0f
    private var lastY = 0f

    /**
     * 터치이벤트 처리
     */
    override fun onTouchEvent(event: MotionEvent): Boolean {
        val x = event.x
        val y = event.y

        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                path.moveTo(x, y)
                lastX = x
                lastY = y
                return true
            }

            MotionEvent.ACTION_MOVE -> {
                path.quadTo(lastX, lastY, (x + lastX) / 2, (y + lastY) / 2)
                lastX = x
                lastY = y
                invalidate()
            }
        }
        return super.onTouchEvent(event)
    }
    fun clearCanvas() {
        path.reset()
        invalidate()
    }

    /**
     * 그린 내용을 비트맵으로 변환하는 메서드
     *
     */
    fun getBitmap(): Bitmap {
        val bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)
        draw(canvas)
        return bitmap
    }

}