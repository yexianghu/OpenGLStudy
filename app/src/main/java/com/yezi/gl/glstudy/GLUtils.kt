package com.yezi.gl.glstudy

import android.opengl.GLES30
import android.util.Log

/**
 * Created by yezi on 2018/5/14.
 */

private val TAG = "GLUtils"

fun glLoadShader(type:Int, shaderSource:String):Int {

    val shader = GLES30.glCreateShader(type)

    if (shader == 0) {
        Log.e(TAG, "create shader failed, type:$type source:$shaderSource")
        return 0
    }

    GLES30.glShaderSource(shader, shaderSource)

    GLES30.glCompileShader(shader)

    val compileResult = intArrayOf()
    GLES30.glGetShaderiv(shader, GLES30.GL_COMPILE_STATUS, compileResult, 0)

    if (compileResult[0] == 0) {
        val infoLen = intArrayOf()
        GLES30.glGetShaderiv(shader, GLES30.GL_INFO_LOG_LENGTH, infoLen, 0)
        if (infoLen[0] > 1) {
            val errMsg = GLES30.glGetShaderInfoLog(shader)
            Log.e(TAG, "Error compile shader: ${errMsg}")
        }
        GLES30.glDeleteShader(shader)
        return 0
    }
    return shader
}

fun glLoadProgram(vertexShader:String, fragmentShader:String):Int {
    val fragmentShader = glLoadShader(GLES30.GL_FRAGMENT_SHADER, fragmentShader)
    val vertexShader = glLoadShader(GLES30.GL_VERTEX_SHADER, vertexShader)

    val program = GLES30.glCreateProgram()
    if (program == 0) {
        return 0
    }

    GLES30.glAttachShader(program, fragmentShader)
    GLES30.glAttachShader(program, vertexShader)

    GLES30.glLinkProgram(program)

    val linkStatusArray = intArrayOf()

    GLES30.glGetProgramiv(program, GLES30.GL_LINK_STATUS, linkStatusArray, 0)

    if (linkStatusArray[0] == 0) {
        val errMsg = GLES30.glGetProgramInfoLog(program)
        Log.e(TAG, "link program error. $errMsg")
        GLES30.glDeleteProgram(program)
        return 0
    }
    return program
}