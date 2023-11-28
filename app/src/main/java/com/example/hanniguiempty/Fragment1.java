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

public class Fragment1 extends Fragment implements SensorEventListener {

    private SensorManager sensorManager;
    private Sensor ambientTemperatureSensor;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_fragment1, container, false);

        // Initialize sensor manager
        sensorManager = (SensorManager) requireActivity().getSystemService(Context.SENSOR_SERVICE);

        // Check ambient temperature sensor availability
        ambientTemperatureSensor = sensorManager.getDefaultSensor(Sensor.TYPE_AMBIENT_TEMPERATURE);
        if (ambientTemperatureSensor != null) {
            // Register the sensor listener
            sensorManager.registerListener(this, ambientTemperatureSensor, SensorManager.SENSOR_DELAY_NORMAL);
        } else {
            // Handle case when sensor is not available
            Toast.makeText(requireContext(), "Ambient temperature sensor not available", Toast.LENGTH_SHORT).show();
        }

        return view;
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if (event.sensor.getType() == Sensor.TYPE_AMBIENT_TEMPERATURE) {
            float temperatureValue = event.values[0];

            // Update UI with the temperature value
            // For example, you might use a TextView to display it
            TextView temperatureTextView = requireView().findViewById(R.id.temperatureTextView);
            temperatureTextView.setText(String.format(Locale.getDefault(), "%.2f °C", temperatureValue));
        }
    }
    // vvv Had to add this to stop the compiler from giving errors
    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        // Handle accuracy changes if needed
    }



    @Override
    public void onDestroyView() {
        // ^^ checks when the view is closed/destoyed
        super.onDestroyView();
        // Deletes the sensor listener when the above is true
        sensorManager.unregisterListener(this);
    }

}


