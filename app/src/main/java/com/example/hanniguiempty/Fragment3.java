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

import java.util.Locale;

public class Fragment3 extends Fragment implements SensorEventListener {

    private SensorManager sensorManager;
    private Sensor gyroscopeSensor;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_fragment3, container, false);

        // Initialize sensor manager
        sensorManager = (SensorManager) requireActivity().getSystemService(Context.SENSOR_SERVICE);

        // Check if gyroscope sensor is available
        gyroscopeSensor = sensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE);
        if (gyroscopeSensor != null) {
            // Register the sensor listener
            sensorManager.registerListener(this, gyroscopeSensor, SensorManager.SENSOR_DELAY_NORMAL);
        } else {
            // Handle case when sensor is not available
            Toast.makeText(requireContext(), "Gyroscope sensor not available", Toast.LENGTH_SHORT).show();
        }

        return view;
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if (event.sensor.getType() == Sensor.TYPE_GYROSCOPE) {
            float xRotationRate = event.values[0];
            float yRotationRate = event.values[1];
            float zRotationRate = event.values[2];

            // Update UI with gyroscope readings
            // For example, you might use TextViews to display the rotation rates
            TextView xRotationTextView = requireView().findViewById(R.id.xRotationTextView);
            xRotationTextView.setText(String.format(Locale.getDefault(), "X Rotation: %.2f rad/s", xRotationRate));

            TextView yRotationTextView = requireView().findViewById(R.id.yRotationTextView);
            yRotationTextView.setText(String.format(Locale.getDefault(), "Y Rotation: %.2f rad/s", yRotationRate));

            TextView zRotationTextView = requireView().findViewById(R.id.zRotationTextView);
            zRotationTextView.setText(String.format(Locale.getDefault(), "Z Rotation: %.2f rad/s", zRotationRate));
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

