package com.example.hanniguiempty;

import android.content.Context;
import android.content.Intent;
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
//


import androidx.fragment.app.Fragment;

import com.example.hanniguiempty.connection.PubSubIntentService;
import com.example.hanniguiempty.outputs.SoundPlayer;
import com.example.hanniguiempty.outputs.VibratorController;

import java.util.Locale;

// TEMPERATURE
public class Fragment1 extends Fragment implements SensorEventListener {

    private SensorManager sensorManager;
    private Sensor ambientTemperatureSensor;

    //
    private SoundPlayer soundPlayer;

    //




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

        //
        soundPlayer = new SoundPlayer(requireContext());


        Intent intent = new Intent(getActivity(), PubSubIntentService.class);
        getActivity().startService(intent);


        return view;
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if (event.sensor.getType() == Sensor.TYPE_AMBIENT_TEMPERATURE) {
            float temperatureValue = event.values[0];

            // Update UI with the temperature value
            TextView temperatureTextView = requireView().findViewById(R.id.temperatureTextView);
            temperatureTextView.setText(String.format(Locale.getDefault(), "%.2f °C", temperatureValue));


            //
            if (temperatureValue > 60.0) {
                // Play sound on loop
                //soundPlayer.playSound();

                // call the mqtt sub & pub function and uses it


            } else {
                // Stop the sound when the temperature is not above 60 degrees
                soundPlayer.stopSound();
            }
            //
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


