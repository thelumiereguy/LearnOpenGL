package com.thelumiereguy.learnopengl

import android.opengl.GLSurfaceView
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.SurfaceView

class MainActivity : AppCompatActivity() {
    private var surfaceView: GLSurfaceView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        surfaceView = findViewById(R.id.surface_view)
    }

    override fun onPause() {
        surfaceView?.onPause()
        super.onPause()
    }

    override fun onResume() {
        surfaceView?.onResume()
        super.onResume()
    }

}