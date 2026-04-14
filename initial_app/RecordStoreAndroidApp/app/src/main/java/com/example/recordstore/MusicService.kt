package com.example.recordstore

import android.app.Service
import android.content.Intent
import android.media.MediaPlayer
import android.os.IBinder

class MusicService : Service() {
    private lateinit var mediaPlayer: MediaPlayer

    override fun onCreate() {
        super.onCreate()
        // Initialize the player with your mp3 file
        mediaPlayer = MediaPlayer.create(this, R.raw.record_store_music)
        mediaPlayer.isLooping = true // Keep it playing in the background
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        val action = intent?.action
        if (action == "START") {
            if (!mediaPlayer.isPlaying) mediaPlayer.start()
        } else if (action == "STOP") {
            if (mediaPlayer.isPlaying) mediaPlayer.pause()
        }
        return START_STICKY
    }

    override fun onDestroy() {
        if (::mediaPlayer.isInitialized) {
            mediaPlayer.stop()
            mediaPlayer.release()
        }
        super.onDestroy()
    }

    override fun onBind(intent: Intent?): IBinder? = null
}