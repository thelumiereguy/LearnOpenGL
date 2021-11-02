package com.thelumiereguy.learnopengl.view

import android.content.Context
import android.opengl.GLSurfaceView
import android.util.AttributeSet
import com.thelumiereguy.learnopengl.renderer.basic.MyGLRenderer

class MyGLSurfaceView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
) : GLSurfaceView(context, attrs) {

    private val renderer: MyGLRenderer

    init {

        // Create an OpenGL ES 2.0 context
        setEGLContextClientVersion(2)

        renderer = MyGLRenderer()

        // Set the Renderer for drawing on the GLSurfaceView
        setRenderer(renderer)
    }
}