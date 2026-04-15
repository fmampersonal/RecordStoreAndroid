package com.example.recordstore.broadcast

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.provider.Settings
import com.example.recordstore.viewmodel.AlbumViewModel

class AirplaneModeReceiver(private val viewModel: AlbumViewModel) : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        if (intent?.action == Intent.ACTION_AIRPLANE_MODE_CHANGED) {
            // Check if airplane mode is actually on or off
            val isAirplaneModeEnabled = Settings.Global.getInt(
                context?.contentResolver,
                Settings.Global.AIRPLANE_MODE_ON, 0
            ) != 0

            // Send the status to the ViewModel (this function already exists in your VM!)
            viewModel.setIsInAirplaneMode(isAirplaneModeEnabled)
        }
    }
}