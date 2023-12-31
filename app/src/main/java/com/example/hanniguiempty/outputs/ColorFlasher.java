package com.example.hanniguiempty.outputs;

import android.content.Context;
import android.graphics.Color;
import android.os.Handler;
import android.os.Looper;
import android.view.View;

public class ColorFlasher {

    private View targetView;
    private Handler handler;
    private boolean isFlashing = false;

    public ColorFlasher(View targetView) {
        this.targetView = targetView;
        this.handler = new Handler(Looper.getMainLooper());
    }

    public void flashRedColor(int durationMillis) {
        if (!isFlashing) {
            isFlashing = true;

            // Flash red color
            targetView.setBackgroundColor(Color.RED);

            // Reset color after the specified duration
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    resetColor();
                }
            }, durationMillis);
        }
    }

    private void resetColor() {
        if (isFlashing) {
            // Reset color to the default state (you can customize this color)
            targetView.setBackgroundColor(Color.TRANSPARENT);
            isFlashing = false;
        }
    }
}
