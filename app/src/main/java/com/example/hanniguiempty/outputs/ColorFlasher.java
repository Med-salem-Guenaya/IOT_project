package com.example.hanniguiempty.outputs;

import android.graphics.Color;
import android.os.Handler;
import android.os.Looper;
import android.view.View;

public class ColorFlasher {
    //
    private View targetView;

    //  creates Handeler that runs the code in different thread that allows
    private Handler handler;

    //
    private boolean isFlashing = false;

    //
    public ColorFlasher(View targetView) {
        this.targetView = targetView;
        this.handler = new Handler(Looper.getMainLooper());
    }

    //  function that has the color flashing logic
    public void flashColor(int durationMillis) {
        //
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

    //  function that Resets the screen/fragment portion color
    private void resetColor() {
        if (isFlashing) {
            // Reset color to the default state (you can customize this color)
            targetView.setBackgroundColor(Color.TRANSPARENT);
            isFlashing = false;
        }
    }
}
