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

import com.example.hanniguiempty.outputs.VibratorController;

import java.util.Locale;

//  PROXIMITY
public class Fragment2 extends Fragment implements SensorEventListener {

    private SensorManager sensorManager;
    private Sensor proximitySensor;

    //vvv
    private VibratorController vibratorController;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_fragment2, container, false);

        // Initialize sensor manager
        sensorManager = (SensorManager) requireActivity().getSystemService(Context.SENSOR_SERVICE);

        // Check if proximity sensor is available
        proximitySensor = sensorManager.getDefaultSensor(Sensor.TYPE_PROXIMITY);
        if (proximitySensor != null) {
            // Register the sensor listener
            sensorManager.registerListener(this, proximitySensor, SensorManager.SENSOR_DELAY_NORMAL);
        } else {
            // Handle case when sensor is not available
            Toast.makeText(requireContext(), "Proximity sensor not available", Toast.LENGTH_SHORT).show();
        }

        //
        vibratorController = new VibratorController(requireContext());

        return view;
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if (event.sensor.getType() == Sensor.TYPE_PROXIMITY) {
            float proximityValue = event.values[0];

            // Update UI with the proximity value
            // For example, you might use a TextView to display it
            TextView proximityTextView = requireView().findViewById(R.id.proximityTextView);
            proximityTextView.setText(String.format(Locale.getDefault(), "Proximity: %.2f", proximityValue));
            //
            if (proximityValue < 5) {
                // Vibrate if the temperature is above 60 degrees
                vibratorController.vibrate(1000);
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

