package me.felixzhang.example.my_particles;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.content.pm.ConfigurationInfo;
import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;


public class ParticlesActivity extends Activity {

    private static final String TAG = ParticlesActivity.class.getSimpleName();
    private GLSurfaceView glSurfaceView;
    private boolean renderSet = false;   //记录GLSurfaceView是否被设置


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        glSurfaceView = new GLSurfaceView(this);

        //检查当前设备是否支持OpenGL ES 2.0
        final ActivityManager activityManager =
                (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);

        ConfigurationInfo configurationInfo = activityManager.getDeviceConfigurationInfo();

        final boolean supportsEs2 = configurationInfo.reqGlEsVersion >= 0x20000;
        Log.i(TAG, "是否支持OpenGL ES 2.0 : " + supportsEs2);

        final ParticlesRenderer renderer=new ParticlesRenderer(this);

        if (supportsEs2) {
            glSurfaceView.setEGLContextClientVersion(2);
            glSurfaceView.setRenderer(renderer);

            renderSet = true;
        } else {
            Toast.makeText(this, "当前设备不支持OpenGL ES 2.0 ", Toast.LENGTH_SHORT).show();
            return;
        }


        setContentView(glSurfaceView);
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (renderSet) {
            glSurfaceView.onPause();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (renderSet) {
            glSurfaceView.onResume();
        }
    }
}
