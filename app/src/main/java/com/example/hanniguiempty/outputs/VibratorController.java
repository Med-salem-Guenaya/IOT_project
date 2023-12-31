package com.example.hanniguiempty.outputs;

import android.content.Context;
import android.os.Build;
import android.os.VibrationEffect;
import android.os.Vibrator;

public class VibratorController {

    private Context context;
    private Vibrator vibrator;

    public VibratorController(Context context) {
        this.context = context;
        this.vibrator = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
    }

    public void vibrate(long milliseconds) {
        if (vibrator != null) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                vibrator.vibrate(VibrationEffect.createOneShot(milliseconds, VibrationEffect.DEFAULT_AMPLITUDE));
            } else {
                // Deprecated in API 26
                vibrator.vibrate(milliseconds);
            }
        }
    }

    public void cancelVibration() {
        if (vibrator != null) {
            vibrator.cancel();
        }
    }
}

