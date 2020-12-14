package com.example.gallery

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Canvas

import android.util.AttributeSet
import android.view.GestureDetector
import android.view.GestureDetector.SimpleOnGestureListener
import android.view.MotionEvent
import android.view.ScaleGestureDetector


class MyImageView(context: Context, attributeSet: AttributeSet) :
    androidx.appcompat.widget.AppCompatImageView(context, attributeSet) {

    private var mGestureDetector = GestureDetector(context, GestureListener())

    private var mScaleDetector: ScaleGestureDetector =
        ScaleGestureDetector(context, ScaleListener())
    private var mScaleFactor: Float = 1f
    private var mBoardScale = 1f


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

        private var currentX = 0
        private var currentY = 0

        override fun onScroll(
            e1: MotionEvent?,
            e2: MotionEvent?,
            distanceX: Float,
            distanceY: Float
        ): Boolean {
            this@MyImageView.width
            with(this@MyImageView) {
                if (scaleX > 1f || scaleY > 1f) {
                    currentY += distanceY.toInt()
                    currentX += distanceX.toInt()
                    scrollTo(currentX, currentY)
                }
            }
            return true
        }

        override fun onDown(e: MotionEvent): Boolean {
            return false
        }

        // event when double tap occurs
        override fun onDoubleTap(e: MotionEvent): Boolean {
            with(this@MyImageView) {
                scaleX = 1f
                scaleY = 1f
                scaleType = ScaleType.FIT_CENTER
                scrollBy(0, 0)
            }
            return true
        }
    }

}

