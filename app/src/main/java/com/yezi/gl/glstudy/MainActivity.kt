package com.yezi.gl.glstudy

import android.opengl.GLSurfaceView
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import javax.microedition.khronos.egl.EGLConfig
import javax.microedition.khronos.opengles.GL10

class MainActivity : AppCompatActivity() {

    private val mRender:GLSurfaceView.Renderer = GLRenderer()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initGLSurfaceView()
    }

    private fun initGLSurfaceView() {
    }
    override fun onPause() {
        super.onPause()
    }

    override fun onResume() {
        super.onResume()
    }
}

class GLRenderer:GLSurfaceView.Renderer {

    override fun onDrawFrame(gl: GL10?) {

    }

    override fun onSurfaceChanged(gl: GL10?, width: Int, height: Int) {
    }

    override fun onSurfaceCreated(gl: GL10?, eglConfig: EGLConfig?) {
    }
}
