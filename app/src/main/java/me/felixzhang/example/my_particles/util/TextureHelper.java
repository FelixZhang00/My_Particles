package me.felixzhang.example.my_particles.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import static android.opengl.GLES20.GL_LINEAR;
import static android.opengl.GLES20.GL_LINEAR_MIPMAP_LINEAR;
import static android.opengl.GLES20.GL_TEXTURE_2D;
import static android.opengl.GLES20.GL_TEXTURE_MAG_FILTER;
import static android.opengl.GLES20.GL_TEXTURE_MIN_FILTER;
import static android.opengl.GLES20.glBindTexture;
import static android.opengl.GLES20.glDeleteTextures;
import static android.opengl.GLES20.glGenTextures;
import static android.opengl.GLES20.glGenerateMipmap;
import static android.opengl.GLES20.glTexParameterf;
import static android.opengl.GLUtils.texImage2D;

/**
 * Created by felix on 15/5/17.
 */
public class TextureHelper {
    private static final String TAG = TextureHelper.class.getSimpleName();

    /**
     * Loads a texture from a resource ID
     *
     * @return OpenGL加载完成后的纹理id;Returns 0 if the load failed.
     */
    public static int loadTexture(Context context, int resId) {
        int[] textureObjectIds = new int[1];
        glGenTextures(1, textureObjectIds, 0);  //生成一个纹理对象

        if (textureObjectIds[0] == 0) {
            if (LoggerConfig.ON) {
                Log.w(TAG, "Could not generate a new OpenGL texture object.");
            }
            return 0;
        }


        //用Android内置的位图解码器把图像文件解压缩为OpenGL能理解的形式
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inScaled = false;

        final Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(), resId, options);

        if (bitmap == null) {
            if (LoggerConfig.ON) {
                Log.w(TAG, "Resource ID" + resId + " could not be decoded.");
            }
            glDeleteTextures(1, textureObjectIds, 0);
            return 0;
        }

        // Bind to the texture in OpenGL
        glBindTexture(GL_TEXTURE_2D, textureObjectIds[0]);

        //设置过滤器，用内置的算法解决放大缩小图片锯齿、失真等情况
        glTexParameterf(GL_TEXTURE_2D,GL_TEXTURE_MIN_FILTER,GL_LINEAR_MIPMAP_LINEAR);
        glTexParameterf(GL_TEXTURE_2D,GL_TEXTURE_MAG_FILTER,GL_LINEAR);

        //加载位图数据到OpenGL
        texImage2D(GL_TEXTURE_2D,0,bitmap,0);

        //生成MIP贴图
        glGenerateMipmap(GL_TEXTURE_2D);

        //Recycle the bitmap,since its data has been loaded into OpenGL.
        bitmap.recycle();

        //Unbind from the texture.
        glBindTexture(GL_TEXTURE_2D,0);

        return textureObjectIds[0];
    }

}
