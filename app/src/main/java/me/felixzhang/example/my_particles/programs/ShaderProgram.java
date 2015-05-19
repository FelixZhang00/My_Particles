package me.felixzhang.example.my_particles.programs;

import android.content.Context;
import android.util.Log;

import me.felixzhang.example.my_particles.util.ShaderHelper;
import me.felixzhang.example.my_particles.util.TextResourceReader;

import static android.opengl.GLES20.glUseProgram;

/**
 * Created by felix on 15/5/17.
 */
abstract class ShaderProgram {
    private static final String TAG = ShaderProgram.class.getSimpleName();
    // Uniform constants
    protected static final String U_MATRIX = "u_Matrix";
    protected static final String U_COLOR = "u_Color";
    protected static final String U_TEXTURE_UNIT = "u_TextureUnit";
    protected static final String U_TIME = "u_Time";

    // Attribute constants
    protected static final String A_POSITION = "a_Position";
    protected static final String A_COLOR = "a_Color";
    protected static final String A_TEXTURE_COORDINATES = "a_TextureCoordinates";

    protected static final String A_DIRECTION_VECTOR = "a_DirectionVector";
    protected static final String A_PARTICLE_START_TIME = "a_ParticleStartTime";


    // Shader program 一个经过编译连接的OpenGL程序编号
    protected final int program;

    protected ShaderProgram(Context context, int vertexShaderResourceId,
                            int fragmentShaderResourceId) {
        // Compile the shaders and link the program.
        program = ShaderHelper.buildProgram(
                TextResourceReader.readTextFileFromResource(
                        context, vertexShaderResourceId),
                TextResourceReader.readTextFileFromResource(
                        context, fragmentShaderResourceId));

        Log.i(TAG, this.getClass().getSimpleName() + "build a program" + program);
    }

    public void useProgram() {
        // Set the current OpenGL shader program to this program.
        glUseProgram(program);

    }


}
