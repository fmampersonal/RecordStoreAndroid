package com.example.buspass

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.example.buspass.screens.MainNavigation
import com.example.buspass.ui.theme.BusPassTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            BusPassTheme {
                MainNavigation()
            }
        }
    }

    override fun onPause() {
        super.onPause()
        println("BJM onPause of MainActivity is running");
    }
}