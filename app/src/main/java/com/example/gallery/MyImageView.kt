package com.example.gallery

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Canvas
import android.graphics.Rect
import android.graphics.RectF
import android.util.AttributeSet
import android.util.Log
import android.view.GestureDetector
import android.view.GestureDetector.SimpleOnGestureListener
import android.view.MotionEvent
import android.view.ScaleGestureDetector


class MyImageView(context: Context, attributeSet: AttributeSet) :
    androidx.appcompat.widget.AppCompatImageView(context, attributeSet) {

    private var mGestureDetector = GestureDetector(context, GestureListener())

    private val mCurrentViewport = RectF(0f, 0f, 10f, 10f)

    private var mScaleDetector: ScaleGestureDetector =
        ScaleGestureDetector(context, ScaleListener())
    private var mScaleFactor: Float = 1f

    private val kMinBoardScale = 0.1f
    private val kMaxBoardScale = 10f
    private var mBoardScale = 1f

    private val mContentRect: Rect? = null

    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(event: MotionEvent?): Boolean {
        super.onTouchEvent(event)
        mScaleDetector.onTouchEvent(event)
        mGestureDetector.onTouchEvent(event)

        return true
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        canvas?.apply {
            save()
            scale(mScaleFactor, mScaleFactor)
            restore()
        }
    }

    inner class ScaleListener : ScaleGestureDetector.SimpleOnScaleGestureListener() {
        override fun onScale(detector: ScaleGestureDetector): Boolean {
            mBoardScale *= detector.scaleFactor

            with(this@MyImageView) {
                scaleX = mBoardScale
                scaleY = mBoardScale
            }
            return true
        }
    }

    inner class GestureListener : SimpleOnGestureListener() {


        override fun onScroll(
            e1: MotionEvent?,
            e2: MotionEvent?,
            distanceX: Float,
            distanceY: Float
        ): Boolean {
            return Math.abs(distanceX) > Math.abs(distanceY) || super.onScroll(
                e1,
                e2,
                distanceX,
                distanceY
            )
        }

        override fun onDown(e: MotionEvent): Boolean {
            return false
        }

        // event when double tap occurs
        override fun onDoubleTap(e: MotionEvent): Boolean {
            with(this@MyImageView) {
                scaleX = 1f
                scaleY = 1f
            }
            return true
        }
    }

}

