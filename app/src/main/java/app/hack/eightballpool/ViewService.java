package app.hack.eightballpool;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.graphics.PixelFormat;
import android.widget.Button;

import androidx.annotation.Nullable;

import myproject.R;

public class ViewService extends Service {

    private WindowManager windowManager;
    private View overlayView;
    private Normal normal;

    @Override
    public void onCreate() {
        super.onCreate();

        overlayView = LayoutInflater.from(this).inflate(R.layout.main, null);
        normal = new Normal(this);

        Button btnNormal = overlayView.findViewById(R.id.btn_normal);
        btnNormal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                normal.setVisibility(View.VISIBLE);
            }
        });

        WindowManager.LayoutParams params = new WindowManager.LayoutParams(
                WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY,
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
                PixelFormat.TRANSLUCENT
        );

        windowManager = (WindowManager) getSystemService(WINDOW_SERVICE);
        windowManager.addView(normal, params);
        windowManager.addView(overlayView, params);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (overlayView != null) windowManager.removeView(overlayView);
        if (normal != null) windowManager.removeView(normal);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
