package me.felixzhang.example.my_particles.util;


import android.util.Log;

import static android.opengl.GLES20.GL_COMPILE_STATUS;
import static android.opengl.GLES20.GL_FRAGMENT_SHADER;
import static android.opengl.GLES20.GL_LINK_STATUS;
import static android.opengl.GLES20.GL_VALIDATE_STATUS;
import static android.opengl.GLES20.GL_VERTEX_SHADER;
import static android.opengl.GLES20.glAttachShader;
import static android.opengl.GLES20.glCompileShader;
import static android.opengl.GLES20.glCreateProgram;
import static android.opengl.GLES20.glCreateShader;
import static android.opengl.GLES20.glDeleteProgram;
import static android.opengl.GLES20.glDeleteShader;
import static android.opengl.GLES20.glGetProgramInfoLog;
import static android.opengl.GLES20.glGetProgramiv;
import static android.opengl.GLES20.glGetShaderInfoLog;
import static android.opengl.GLES20.glGetShaderiv;
import static android.opengl.GLES20.glLinkProgram;
import static android.opengl.GLES20.glShaderSource;
import static android.opengl.GLES20.glValidateProgram;

/**
 * Created by felix on 15/5/16.
 * <p/>
 * 帮助编译着色器代码
 */
public class ShaderHelper {

    private static final String TAG = ShaderHelper.class.getSimpleName();

    /**
     * 编译着色器，链接在一起成为一个程序
     *
     * @param vertexShaderSource
     * @param fragmentShaderSource
     * @return
     */
    public static int buildProgram(String vertexShaderSource, String fragmentShaderSource) {
        int program;

        //Compile the shaders.
        int vertexShader = compileVertexShader(vertexShaderSource);
        int fragmentShader = compileFragmentShader(fragmentShaderSource);

        //Link them into a shader program.
        program = linkProgram(vertexShader, fragmentShader);

        if (LoggerConfig.ON) {
            vaildateProgram(program);
        }

        return program;

    }


    public static int compileVertexShader(String shaderCode) {
        return compileShader(GL_VERTEX_SHADER, shaderCode);
    }

    public static int compileFragmentShader(String shaderCode) {
        return compileShader(GL_FRAGMENT_SHADER, shaderCode);
    }


    /**
     * Compiles a shader,returning the OpenGL object ID
     */
    private static int compileShader(int type, String shaderCode) {

        //Create a new shader object
        final int shaderObjectId = glCreateShader(type);

        if (shaderObjectId == 0) {
            if (LoggerConfig.ON) {
                Log.w(TAG, "Could not create new shader.");
            }

            return 0;
        }

        //Pass in the shader source
        glShaderSource(shaderObjectId, shaderCode);

        //Compile the shader
        glCompileShader(shaderObjectId);

        final int[] compileStatus = new int[1];
        glGetShaderiv(shaderObjectId, GL_COMPILE_STATUS, compileStatus, 0);

        if (LoggerConfig.ON) {
            //Print the shader info log to the Android log output
            Log.v(TAG, "Results of compiling source:" + "\n" + shaderCode + "\n:" +
                    glGetShaderInfoLog(shaderObjectId));
        }

        //Verify the compile status
        if (compileStatus[0] == 0) {
            //If it failed,delete the shader object.
            glDeleteShader(shaderObjectId);

            if (LoggerConfig.ON) {
                Log.w(TAG, "Compilation of shader failed.");
            }

            return 0;
        }

        //Return the shader object ID
        return shaderObjectId;

    }


    /**
     * Links a vertex shader and a fragment shader together into an OpenGL
     * program. Returns the OpenGL program object ID,or 0 if linking is failed.
     */
    public static int linkProgram(int vertexShaderId, int fragmentShaderId) {

        //Create a new program object.
        final int programObjectId = glCreateProgram();

        if (programObjectId == 0) {
            if (LoggerConfig.ON) {
                Log.w(TAG, "Could not create new program");
            }
            return 0;
        }

        //Attach the vertex shader to the program.
        glAttachShader(programObjectId, vertexShaderId);

        //Attach the fragment shader to the program.
        glAttachShader(programObjectId, fragmentShaderId);

        //Link the two shaders together into a program.
        glLinkProgram(programObjectId);

        //Get the Link status
        final int[] linkStatus = new int[1];
        glGetProgramiv(programObjectId, GL_LINK_STATUS, linkStatus, 0);

        if (LoggerConfig.ON) {
            Log.v(TAG, "Results of linking program :\n" +
                    glGetProgramInfoLog(programObjectId));
        }

        //Verify the link status
        if (linkStatus[0] == 0) {
            //If it failed,delete the program object
            glDeleteProgram(programObjectId);
            if (LoggerConfig.ON) {
                Log.w(TAG, "Linking of program failed.");
            }

            return 0;
        }

        //Returns the program object ID.
        return programObjectId;

    }


    /**
     * Vaildates an OpenGL program. Should only be called when developing the application.
     */
    public static boolean vaildateProgram(int programObjectId) {
        glValidateProgram(programObjectId);

        final int[] vaildateStatus = new int[1];
        glGetProgramiv(programObjectId, GL_VALIDATE_STATUS, vaildateStatus, 0);

        Log.v(TAG, "Results of validating program:" + vaildateStatus[0]
                + "\nLog: " + glGetProgramInfoLog(programObjectId));

        return vaildateStatus[0] != 0;

    }


}
