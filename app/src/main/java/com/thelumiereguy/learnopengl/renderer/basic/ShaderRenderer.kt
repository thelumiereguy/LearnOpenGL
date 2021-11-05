package com.thelumiereguy.learnopengl.renderer.basic

import android.opengl.GLES20.*
import android.opengl.GLSurfaceView
import android.util.Log
import com.thelumiereguy.learnopengl.renderer.basic.shaders.createAndVerifyShader
import com.thelumiereguy.learnopengl.renderer.basic.shaders.fragmentShader
import com.thelumiereguy.learnopengl.renderer.basic.shaders.vertexShader
import java.nio.ByteBuffer
import java.nio.ByteOrder
import javax.microedition.khronos.egl.EGLConfig
import javax.microedition.khronos.opengles.GL10


class AirHockeyRenderer : GLSurfaceView.Renderer {

    private val POSITION_COMPONENT_COUNT = 2

    private val tableVertices by lazy {
        floatArrayOf(
            0f, 0f,
            -1f, -1f,
            1f, -1f,
            1f, 1f,
            -1f, 1f,
            -1f, -1f
        )
    }

    private val bytesPerFloat = 4

    val verticesData by lazy {
        ByteBuffer.allocateDirect(tableVertices.size * bytesPerFloat)
            .order(ByteOrder.nativeOrder())
            .asFloatBuffer().also {
                it.put(tableVertices)
            }
    }

    private val fragShader by lazy {
        createAndVerifyShader(fragmentShader, GL_FRAGMENT_SHADER)
    }

    private val vertShader by lazy {
        createAndVerifyShader(vertexShader, GL_VERTEX_SHADER)
    }


    override fun onSurfaceCreated(gl: GL10?, config: EGLConfig?) {
        glClearColor(0f, 0f, 0f, 1f)
        glDisable(GL10.GL_DITHER)
        glHint(GL10.GL_PERSPECTIVE_CORRECTION_HINT, GL10.GL_FASTEST)

        val programId = glCreateProgram()
        if (programId == 0) {
            Log.d("glCreateProgram", "Could not create new program")
        }

        glAttachShader(programId, vertShader)
        glAttachShader(programId, fragShader)

        glLinkProgram(programId)

        val linkStatus = IntArray(1)
        glGetProgramiv(programId, GL_LINK_STATUS, linkStatus, 0)


        Log.d(
            "Results of linking ",
            glGetProgramInfoLog(programId)
        )

        if (linkStatus[0] == 0) {
            glDeleteProgram(programId)
            Log.w("linkStatus", "Linking of program failed.");
        }

        if (validateProgram(programId)) {
            attribLocation = glGetAttribLocation(programId, "a_Position")
            uniformLocation = glGetUniformLocation(programId, "u_Color")
            resolutionUniformLocation = glGetUniformLocation(programId, "u_resolution")
            timeUniformLocation = glGetUniformLocation(programId, "u_time")
        } else {
            Log.w("validateProgram", "Validating of program failed.");
            return
        }

        verticesData.position(0)

        glVertexAttribPointer(
            attribLocation!!,
            POSITION_COMPONENT_COUNT,
            GL_FLOAT,
            false,
            0,
            verticesData
        )

        glEnableVertexAttribArray(attribLocation!!)

        glUseProgram(programId)
    }

    var attribLocation: Int? = null
    var uniformLocation: Int? = null
    var resolutionUniformLocation: Int? = null
    var timeUniformLocation: Int? = null

    private var surfaceHeight = 0f
    private var surfaceWidth = 0f

    override fun onSurfaceChanged(gl: GL10?, width: Int, height: Int) {
        glViewport(0, 0, width, height)
        surfaceWidth = width.toFloat()
        surfaceHeight = height.toFloat()
        val aspectRatio = (width / height).toFloat()
//        gl.glLoadMatrixf()
        gl?.glLoadIdentity()
        frameCount = 0f
    }

    private var frameCount = 0f

    override fun onDrawFrame(gl: GL10?) {
        glDisable(GL10.GL_DITHER)
        glClear(GL10.GL_COLOR_BUFFER_BIT)

        uniformLocation?.let {
            glUniform4f(it, 1f, 1f, 1f, 1f)
        }

        resolutionUniformLocation?.let {
            glUniform2f(it, surfaceWidth, surfaceHeight)
        }

        timeUniformLocation?.let {
            glUniform1f(it, frameCount)
        }

        glDrawArrays(GL_TRIANGLE_FAN, 0, 6)

        if (frameCount > 10) {
            frameCount = 0f
        }

        frameCount += 0.01f
    }


    fun validateProgram(programObjectId: Int): Boolean {
        glValidateProgram(programObjectId)
        val validateStatus = IntArray(1)
        glGetProgramiv(programObjectId, GL_VALIDATE_STATUS, validateStatus, 0)
        Log.v(
            "Results of validating", """ ${validateStatus[0]}
                    Log : ${glGetProgramInfoLog(programObjectId)}
        """.trimIndent()
        )

        return validateStatus[0] != 0
    }

}
