package com.example.hanniguiempty.outputs;

import android.content.Context;
import android.content.pm.PackageManager;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraCharacteristics;
import android.hardware.camera2.CameraManager;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.os.VibrationEffect;
import android.os.Vibrator;

public class FlashController {

    private Context context;
    private CameraManager cameraManager;
    private String cameraId;
    private boolean isFlashOn;
    private Vibrator vibrator;

    public FlashController(Context context) {
        this.context = context;

        // Check if the device has a camera with flash
        if (context.getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA_FLASH)) {
            cameraManager = (CameraManager) context.getSystemService(Context.CAMERA_SERVICE);

            try {
                // Get the camera ID with flash
                for (String id : cameraManager.getCameraIdList()) {
                    CameraCharacteristics characteristics = cameraManager.getCameraCharacteristics(id);
                    if (characteristics.get(CameraCharacteristics.FLASH_INFO_AVAILABLE)) {
                        cameraId = id;
                        break;
                    }
                }

                // Check if the camera with flash is available
                if (cameraId == null) {
                    throw new RuntimeException("Camera with flash not available");
                }
            } catch (CameraAccessException e) {
                e.printStackTrace();
            }

            // Initialize vibrator
            vibrator = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
        } else {
            throw new RuntimeException("Camera with flash not supported on this device");
        }
    }

    public void turnOnFlashForInterval(long durationMillis) {
        if (cameraManager != null) {
            try {
                // Turn on the flash
                cameraManager.setTorchMode(cameraId, true);
                isFlashOn = true;


                // Turn off the flash after the specified duration
                new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        turnOffFlash();
                    }
                }, durationMillis);

            } catch (CameraAccessException e) {
                e.printStackTrace();
            }
        }
    }

    private void turnOffFlash() {
        if (cameraManager != null) {
            try {
                // Turn off the flash
                cameraManager.setTorchMode(cameraId, false);
                isFlashOn = false;

            } catch (CameraAccessException e) {
                e.printStackTrace();
            }
        }
    }


    }


