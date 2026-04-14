package ca.hccis.buspass

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.calculateWindowSizeClass
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import ca.hccis.buspass.bo.BusPassBO
import ca.hccis.buspass.viewmodel.BusPassViewModel
import ca.hccis.buspass.viewmodel.BusPassViewModelFactory
import ca.hccis.buspass.bo.NotificationBO
import ca.hccis.buspass.bo.SplashScreenBO
import ca.hccis.buspass.broadcast.receiver.AirplaneModeReceiver
import ca.hccis.buspass.db.BusPassDatabase
import ca.hccis.buspass.db.BusPassRepository
import ca.hccis.buspass.entity.BusPass
import ca.hccis.buspass.screens.MainNavigation
import ca.hccis.buspass.utility.CisUtility
import ca.hccis.buspass.ui.theme.BusPassTheme
import np.com.bimalkafle.easybot.viewModel.ChatViewModel

class MainActivity : ComponentActivity() {

    /**********************************
    airplaneModeReceiver
    NOTE:  from GeeksforGeeks:
    Nov 10, 2022 — The “lateinit” keyword in Kotlin as the name suggests is used to declare those
    variables that are guaranteed to be initialized in the future.
     **********************************/
    private lateinit var airplaneModeReceiver: AirplaneModeReceiver

    @OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        SplashScreenBO.handleSplashScreen(this)

        // Request calendar permissions before proceeding to avoid runtime issues
        requestCalendarPermission { isGranted ->
            if (isGranted) {
                Log.d("Permissions", "Calendar permissions granted.")
            } else {
                Toast.makeText(this, "Calendar permissions are required for reminders!", Toast.LENGTH_LONG).show()
            }
        }

        // Initialize database and repository
        val database = BusPassDatabase.getInstance(applicationContext)
        val repository = BusPassRepository(database.busPassDao)
        val viewModelFactory = BusPassViewModelFactory(repository)
        val busPassViewModel =
            ViewModelProvider(this, viewModelFactory)[BusPassViewModel::class.java]

        //********************************************************
        // Broadcast Receiver code below
        //********************************************************

        // Initialize the receiver
        airplaneModeReceiver = AirplaneModeReceiver()
        airplaneModeReceiver.setViewModel(busPassViewModel) //See Kotlin setters are automatic.
        busPassViewModel.contextForToast = applicationContext

        // Register the receiver to listen for airplane mode changes
        val intentFilter =
            android.content.IntentFilter(android.content.Intent.ACTION_AIRPLANE_MODE_CHANGED)
        registerReceiver(airplaneModeReceiver, intentFilter)

        //********************************************************
        // Broadcast Receiver - End
        //********************************************************

        //*****************************************************************************************
        //Notification creation
        //Note that this has been moved to it's own class.  Note how a reference to this
        //MainActivity is passed in to allow the functionality to work.
        //*****************************************************************************************

        val notificationBO: NotificationBO = NotificationBO(this);
        notificationBO.createNotificationChannel()
        val chatViewModel = ViewModelProvider(this)[ChatViewModel::class.java]

        enableEdgeToEdge()
        setContent {
            val windowSizeClass = calculateWindowSizeClass(this)
            BusPassTheme {
                MainNavigation(busPassViewModel, notificationBO, chatViewModel, windowSizeClass)
            }
        }
    }

    override fun onPause() {
        super.onPause()
        println("BJM onPause of MainActivity is running");
    }

    /**
     * ActivityResultLauncher for handling multiple permission requests.
     * This callback is triggered after the user responds to the permission request dialog.
     */
    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        val writeGranted = permissions[Manifest.permission.WRITE_CALENDAR] ?: false
        val readGranted = permissions[Manifest.permission.READ_CALENDAR] ?: false

        // Check if both read and write permissions are granted
        if (writeGranted && readGranted) {
            Log.d("Permissions", "Calendar permissions granted.")
            Toast.makeText(this, "Calendar Permission Granted", Toast.LENGTH_SHORT).show()
        } else {
            Log.e("Permissions", "Calendar permissions denied.")
            Toast.makeText(this, "Calendar Permission Denied. Cannot set reminders.", Toast.LENGTH_LONG).show()
        }
    }

    /**
     * Requests calendar permissions if they are not already granted.
     * This function first checks if permissions exist and only requests them if necessary.
     *
     * @param callback A function that receives a boolean indicating whether the permission is granted.
     */
    private fun requestCalendarPermission(callback: (Boolean) -> Unit) {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_CALENDAR) == PackageManager.PERMISSION_GRANTED &&
            ContextCompat.checkSelfPermission(this, Manifest.permission.READ_CALENDAR) == PackageManager.PERMISSION_GRANTED) {
            Log.d("Permissions", "Calendar permissions already granted.")
            callback(true)
        } else {
            Log.d("Permissions", "Requesting Calendar permissions.")
            requestPermissionLauncher.launch(
                arrayOf(Manifest.permission.WRITE_CALENDAR, Manifest.permission.READ_CALENDAR)
            )
            callback(false)
        }
    }

    /**
     * Adds a bus pass reminder to the Google Calendar if the required permission is granted.
     * This function ensures that calendar events can only be added if WRITE_CALENDAR permission is available.
     *
     * @param busPass The bus pass entity containing details such as date, time, and user information.
     */
    private fun addBusPassAndSetReminder(busPass: BusPass) {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_CALENDAR) == PackageManager.PERMISSION_GRANTED) {
            // Add the bus pass event to the calendar
            BusPassBO.addBusPassReminder(this, busPass)
            Toast.makeText(this, "Bus Pass Reminder Set in Google Calendar", Toast.LENGTH_SHORT).show()
        } else {
            Log.e("Permissions", "Cannot add reminder - Calendar permissions not granted.")
            Toast.makeText(this, "Cannot add reminder - Calendar permissions not granted.", Toast.LENGTH_LONG).show()
        }
    }

}