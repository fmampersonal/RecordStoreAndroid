package ca.hccis.buspass.service;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.IBinder;
import android.util.Log;

import ca.hccis.buspass.R;

public class BackgroundMusicService extends Service {
    private static final String TAG = "BackgroundMusicService";
    private MediaPlayer mediaPlayer;

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(TAG, "Service created");

        // Initialize the MediaPlayer and set the audio file to be played
        mediaPlayer = MediaPlayer.create(this, R.raw.music);
        mediaPlayer.setLooping(false); // Loop the playback
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d(TAG, "Service started");

        // Start playing the tune
        if (!mediaPlayer.isPlaying()) {
            mediaPlayer.start();
        }

        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "Service destroyed");

        // Stop and release the MediaPlayer when the service is destroyed
        if (mediaPlayer != null) {
            mediaPlayer.stop();
            mediaPlayer.release();
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
