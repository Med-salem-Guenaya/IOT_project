package com.example.hanniguiempty;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.example.hanniguiempty.outputs.ColorFlasher;

import java.util.Locale;

public class Fragment4 extends Fragment implements SensorEventListener {

    private SensorManager sensorManager;
    private Sensor accelerometerSensor;

    //
    private ColorFlasher colorFlasher;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_fragment4, container, false);

        // Initialize sensor manager
        sensorManager = (SensorManager) requireActivity().getSystemService(Context.SENSOR_SERVICE);

        // Check if accelerometer sensor is available
        accelerometerSensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        if (accelerometerSensor != null) {
            // Register the sensor listener
            sensorManager.registerListener(this, accelerometerSensor, SensorManager.SENSOR_DELAY_NORMAL);
        } else {
            // Handle case when sensor is not available
            Toast.makeText(requireContext(), "Accelerometer sensor not available", Toast.LENGTH_SHORT).show();
        }

        //
        colorFlasher = new ColorFlasher(view);

        return view;
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            float xAcceleration = event.values[0];
            float yAcceleration = event.values[1];
            float zAcceleration = event.values[2];

            // Update UI with accelerometer readings
            // For example, you might use TextViews to display the acceleration values
            TextView xAccelerationTextView = requireView().findViewById(R.id.xAccelerationTextView);
            xAccelerationTextView.setText(String.format(Locale.getDefault(), "X Acceleration: %.2f m/s²", xAcceleration));

            TextView yAccelerationTextView = requireView().findViewById(R.id.yAccelerationTextView);
            yAccelerationTextView.setText(String.format(Locale.getDefault(), "Y Acceleration: %.2f m/s²", yAcceleration));

            TextView zAccelerationTextView = requireView().findViewById(R.id.zAccelerationTextView);
            zAccelerationTextView.setText(String.format(Locale.getDefault(), "Z Acceleration: %.2f m/s²", zAcceleration));

            //
            if (xAcceleration > 3.0) {
                colorFlasher.flashRedColor(500);
            }

        }

    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        // Handle accuracy changes if needed
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        // Unregister the sensor listener when the fragment is destroyed
        sensorManager.unregisterListener(this);
    }
}

