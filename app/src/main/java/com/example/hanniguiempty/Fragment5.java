package com.example.hanniguiempty;

import android.content.pm.PackageManager;
import android.media.AudioFormat;
import android.media.AudioRecord;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;
import android.Manifest;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import java.util.Locale;

public class Fragment5 extends Fragment {

    private static final int REQUEST_RECORD_AUDIO_PERMISSION = 200;

    private AudioRecord audioRecord;
    private boolean isRecording = false;
    private int bufferSize;
    private short[] audioData;

    private TextView micLevelTextView;
    private Handler handler;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_fragment5, container, false);

        micLevelTextView = view.findViewById(R.id.micLevelTextView);

        handler = new Handler();

        // Check for record audio permission
        if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED) {
            // Permission is not granted, request it
            requestPermissions(new String[]{Manifest.permission.RECORD_AUDIO}, REQUEST_RECORD_AUDIO_PERMISSION);
        } else {
            // Permission is already granted, initialize audio recorder
            initializeAudioRecorder();
        }

        return view;
    }

    private void initializeAudioRecorder() {
        int minBufferSize = AudioRecord.getMinBufferSize(44100, AudioFormat.CHANNEL_IN_MONO, AudioFormat.ENCODING_PCM_16BIT);
        bufferSize = minBufferSize > 0 ? minBufferSize : 1024;
        audioData = new short[bufferSize / 2]; // 16-bit samples, so divide by 2

        audioRecord = new AudioRecord(
                MediaRecorder.AudioSource.MIC,
                44100,
                AudioFormat.CHANNEL_IN_MONO,
                AudioFormat.ENCODING_PCM_16BIT,
                bufferSize
        );

        startRecording();
    }

    private void startRecording() {
        if (audioRecord != null && audioRecord.getState() == AudioRecord.STATE_INITIALIZED) {
            audioRecord.startRecording();
            isRecording = true;
            updateMicLevel();
        }
    }

    private void stopRecording() {
        if (audioRecord != null && audioRecord.getRecordingState() == AudioRecord.RECORDSTATE_RECORDING) {
            audioRecord.stop();
            isRecording = false;
        }
    }

    private void updateMicLevel() {
        new Thread(() -> {
            while (isRecording) {
                int readSize = audioRecord.read(audioData, 0, bufferSize / 2);

                if (readSize > 0) {
                    double sum = 0;
                    for (int i = 0; i < readSize; i++) {
                        sum += audioData[i] * audioData[i];
                    }

                    final double amplitude = Math.sqrt(sum / readSize);
                    handler.post(() -> updateMicLevelText(amplitude));
                }
            }
        }).start();
    }

    private void updateMicLevelText(double amplitude) {
        // Convert amplitude to dB
        double amplitudeDb = 20 * Math.log10(amplitude);

        // Update UI with microphone level
        micLevelTextView.setText(String.format(Locale.getDefault(), "Mic Level: %.2f dB", amplitudeDb));
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        stopRecording();
        releaseAudioRecorder();
    }

    private void releaseAudioRecorder() {
        if (audioRecord != null) {
            audioRecord.release();
            audioRecord = null;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == REQUEST_RECORD_AUDIO_PERMISSION) {
            // Check if the user granted the permission
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission granted, initialize audio recorder
                initializeAudioRecorder();
            } else {
                // Permission denied, handle accordingly (show a message, disable functionality, etc.)
                Toast.makeText(requireContext(), "Permission denied. App cannot access microphone.", Toast.LENGTH_SHORT).show();
            }
        }
    }
}



