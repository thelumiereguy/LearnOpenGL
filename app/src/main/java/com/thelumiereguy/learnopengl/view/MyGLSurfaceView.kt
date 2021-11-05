package com.thelumiereguy.learnopengl.view

import android.content.Context
import android.opengl.GLSurfaceView
import android.util.AttributeSet
import com.thelumiereguy.learnopengl.renderer.basic.ShaderRenderer

class MyGLSurfaceView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
) : GLSurfaceView(context, attrs) {

    private val renderer: ShaderRenderer

    init {

        // Create an OpenGL ES 2.0 context
        setEGLContextClientVersion(2)

        renderer = ShaderRenderer()

        // Set the Renderer for drawing on the GLSurfaceView
        setRenderer(renderer)
    }
}