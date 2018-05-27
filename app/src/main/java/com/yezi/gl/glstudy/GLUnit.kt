package com.yezi.gl.glstudy

import android.opengl.GLES10

interface GLUnit {
    fun init()
    fun draw(gl: GLES10)
}


abstract class GLUnitBase: GLUnit {

    private val mProgram:Int

    init {
        mProgram = glLoadProgram(vertexShaderCode, fragmentShaderCode)
    }

    override fun draw(gl: GLES10) {
        //GLES30.glViewport()
        //GLES30.glUseProgram(mProgram)

        //GLES30.glClearColor(1.0f, 1.0f, 1.0f, 1.0f)
    }

    protected abstract val fragmentShaderCode:String
    protected abstract val vertexShaderCode:String
}

class Triangle: GLUnitBase() {
    override fun init() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override val fragmentShaderCode =
            """#version 300 es
                layout(location = 0) in vec4 vPosition;
                void main() {
                    gl_Position = vPosition
                }
            """.trimIndent()

    override val vertexShaderCode =
            """#version 300 es
                precision mediump float;
                out vec4 fragColor;
                void main() {
                    fragColor = vec4(1.0, 0.0, 0.0, 1.0);
                }
            """.trimIndent()

}