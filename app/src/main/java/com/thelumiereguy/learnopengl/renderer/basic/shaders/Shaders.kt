package com.thelumiereguy.learnopengl.renderer.basic.shaders

import android.opengl.GLES20.*
import android.util.Log

const val vertexShader = """
    attribute vec4 a_Position;
    
    void main (){
        gl_Position = a_Position;
    }
"""

const val fragmentShader = """
    precision mediump float;
    
    uniform vec4 u_Color;
    uniform vec2 u_resolution;
    uniform float u_time;
    
    const float scale=10.;

    mat2 rotate(float angle){
        return mat2(cos(angle),-sin(angle),sin(angle),cos(angle));
    }

    void main(){
         vec2 coord=((gl_FragCoord.xy)/u_resolution)-.5;
            coord.x*=u_resolution.x/u_resolution.y;
            vec3 color=vec3(.1);
    
    vec2 newCoords=coord*rotate(
        u_time+length((coord)*exp2(4.)
    ));
    
    float x=abs(sin(newCoords.x*3.14))+.3;
    float y=abs(sin(newCoords.y))*.1+.3;
    color+=exp(1.-(x/y));
    
    gl_FragColor=vec4(color,1.);
    }
"""

fun createAndVerifyShader(shaderCode: String, shaderType: Int): Int {
    val shaderId = glCreateShader(shaderType)
    if (shaderId == 0) {
        Log.d("createAndVerifyShader", "Create Shader failed")
    }

    glShaderSource(shaderId, shaderCode)
    glCompileShader(shaderId)

    val compileStatusArray = IntArray(1)
    glGetShaderiv(shaderId, GL_COMPILE_STATUS, compileStatusArray, 0)
    Log.d(
        "Results of compiling:", "\n" + shaderCode + "\n:"
                + glGetShaderInfoLog(shaderId)
    )

    if (compileStatusArray.first() == 0) {
        glDeleteShader(shaderId)
        return 0
    }

    return shaderId
}
