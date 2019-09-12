package com.asusuigbo.frank.asusuigbo

import android.graphics.Camera
import android.view.View
import android.view.animation.AccelerateDecelerateInterpolator
import android.view.animation.Animation
import android.view.animation.Transformation


class FlipAnimation(from: View, to: View) : Animation() {

    private lateinit var camera: Camera
    private var fromView: View = from
    private var toView: View = to
    private var xCenter: Float = 0.0f
    private var yCenter: Float = 0.0f
    private var isFront = true

    init{
        duration = 700
        fillAfter = false
        interpolator = AccelerateDecelerateInterpolator()
    }

    fun reverse(){
        isFront = false
        val placeHolder = this.toView
        this.toView = this.fromView
        this.fromView = placeHolder
    }

    override fun initialize(width: Int, height: Int, parentWidth: Int, parentHeight: Int) {
        super.initialize(width, height, parentWidth, parentHeight)
        this.xCenter = (width/2).toFloat()
        this.yCenter = (height/2).toFloat()
        this.camera = Camera()
    }

    override fun applyTransformation(interpolatedTime: Float, t: Transformation?) {
        super.applyTransformation(interpolatedTime, t)

        //val radians = Math.PI * interpolatedTime
        var degrees = 180.0f * interpolatedTime //(180.0 * radians / Math.PI).toFloat()


        // Once we reach the midpoint in the animation, we need to hide the
        // source view and show the destination view. We also need to change
        // the angle by 180 degrees so that the destination does not come in
        // flipped around
        if (interpolatedTime >= 0.5f) {
            degrees -= 180f
            fromView.visibility = View.GONE
            toView.visibility = View.VISIBLE
        }

        if (isFront)
            degrees = -degrees //determines direction of rotation when flip begins

        val matrix = t!!.matrix
        camera.save()
        camera.rotateX(degrees)//camera.rotateY(degrees)
        camera.getMatrix(matrix)
        camera.restore()
        matrix.preTranslate(-this.xCenter, -this.yCenter)
        matrix.postTranslate(xCenter, yCenter)
    }
    //Adapted from
    //https://2cupsoftech.wordpress.com/2012/09/18/3d-flip-between-two-view-or-viewgroup-on-android/
}