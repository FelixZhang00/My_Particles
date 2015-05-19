package me.felixzhang.example.my_particles.util;

import android.content.Context;
import android.content.res.Resources;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * Created by felix on 15/5/16.
 * <p/>
 * 从资源文件读取着色器文本的工具类
 */
public class TextResourceReader {


    public static String readTextFileFromResource(Context context, int resId) {
        StringBuilder body = new StringBuilder();

        try {
            InputStream is = context.getResources().openRawResource(resId);
            InputStreamReader reader = new InputStreamReader(is);
            BufferedReader bufferedReader = new BufferedReader(reader);

            String nextLine;

            while ((nextLine = bufferedReader.readLine()) != null) {
                body.append(nextLine);
                body.append('\n');
            }
        } catch (IOException e) {
            throw new RuntimeException(
                    "Cound not open resource:" + resId, e
            );
        } catch (Resources.NotFoundException e) {
            throw new RuntimeException("Resource not found: " + resId, e);
        }

        return body.toString();
    }

}
