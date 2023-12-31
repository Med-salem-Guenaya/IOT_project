package com.example.hanniguiempty.outputs;

import android.content.Context;
import android.media.MediaPlayer;

import com.example.hanniguiempty.R;

import android.content.Context;
import android.media.MediaPlayer;

public class SoundPlayer {

    private Context context;
    private MediaPlayer mediaPlayer;

    public SoundPlayer(Context context) {
        this.context = context;
        this.mediaPlayer = MediaPlayer.create(context, R.raw.alarm); // Replace with your sound file
        configureMediaPlayerLoop();
    }

    private void configureMediaPlayerLoop() {
        if (mediaPlayer != null) {
            mediaPlayer.setLooping(true);
        }
    }

    public void playSound() {
        if (mediaPlayer != null && !mediaPlayer.isPlaying()) {
            mediaPlayer.start();
        }
    }

    public void stopSound() {
        if (mediaPlayer != null && mediaPlayer.isPlaying()) {
            mediaPlayer.pause();
            mediaPlayer.seekTo(0); // Rewind to the beginning
        }
    }

    public void release() {
        if (mediaPlayer != null) {
            mediaPlayer.release();
            mediaPlayer = null;
        }
    }
}
