package com.yezi.gl.glstudy

import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.view.SurfaceHolder
import android.view.SurfaceView
import javax.microedition.khronos.egl.EGL10
import javax.microedition.khronos.egl.EGLConfig
import javax.microedition.khronos.egl.EGLContext
import javax.microedition.khronos.egl.EGLDisplay


class MyGLSurfaceView:SurfaceView, SurfaceHolder.Callback2 {

    private val mGLThread = GLThread(this)

    constructor(context: Context?) : super(context) {
        init()
    }
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs) {
        init()
    }
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        init()
    }

    private fun init() {
        holder.addCallback(this)
    }

    override fun surfaceRedrawNeeded(p0: SurfaceHolder?) {
    }

    override fun surfaceChanged(holder: SurfaceHolder?, p1: Int, p2: Int, p3: Int) {
    }

    override fun surfaceDestroyed(holder: SurfaceHolder?) {
        mGLThread.stop()
    }

    override fun surfaceCreated(holder: SurfaceHolder?) {
        mGLThread.start()
    }

    fun onPause() {
        mGLThread.pause()
    }

    fun onResume() {
        mGLThread.resume()
    }
}

class GLThread(private val surfaceView:MyGLSurfaceView) : Runnable {

    private val TAG = "GLThread"

    private var mThread:Thread? = null

    @Volatile
    private var mRequestRender = false

    @Volatile
    private var mStopped = false

    @Volatile
    private var mPaused = false

    private var mLock = Object()

    private var mEGLHelper = EGLHelper()

    fun start() {
        if (mThread == null) {
            mThread = Thread(this, "GLThread")
        }
        mStopped = false
        mThread!!.start()
    }

    fun stop() {
        mStopped = true
        mThread = null
        mLock.notify()
    }

    fun pause() {
        mPaused = true
    }

    fun resume() {
        mPaused = false
        mLock.notify()
    }

    fun readyToDraw():Boolean {
        return mRequestRender && !mPaused
    }

    override fun run() {
        Log.d(TAG, "GLThread entry")
        mEGLHelper.initEGL()

        while(!mStopped) {
            if (readyToDraw()) {
                draw()
            } else {
                Log.d(TAG, "GLThread wait")
                synchronized(mLock) {
                    mLock.wait()
                }
            }
        }

        deInitEGL()
        Log.d(TAG, "GLThread exit")
    }

    private fun deInitEGL() {

    }


    public fun requestRender() {
        mRequestRender = true
        mLock.notify()
    }

    private fun draw() {
    }
}

private class EGLHelper {

    private val TAG = "EGLHelper"

    private lateinit var mEGL:EGL10
    private lateinit var mEGLDisplay:EGLDisplay

    fun initEGL() {
        mEGL = EGLContext.getEGL() as EGL10
        mEGLDisplay = mEGL.eglGetDisplay(EGL10.EGL_DEFAULT_DISPLAY)
        if (mEGLDisplay == EGL10.EGL_NO_DISPLAY) {
            throw RuntimeException("egl no display")
        }

        var versions = IntArray(2)
        if (!mEGL.eglInitialize(mEGLDisplay, versions)) {
            throw RuntimeException("egl init failed")
        }
        Log.d(TAG, "egl version: ${versions[0]}.${versions[1]}")

        val numConfigs = intArrayOf(1)
        if(!mEGL.eglGetConfigs(mEGLDisplay, null, -1, numConfigs)) {
            throw RuntimeException("egl getConfig count failed")
        }

        val eglConfigs = arrayOfNulls<EGLConfig>(numConfigs[0])
        if(!mEGL.eglGetConfigs(mEGLDisplay, eglConfigs, numConfigs[0], null)) {
            throw RuntimeException("egl getConfig failed")
        }

        mEGL.eglChooseConfig()
        eglConfigs.forEach {
            mEGL.eglGetConfigAttrib(mEGLDisplay, it, )
        }

    }
}
