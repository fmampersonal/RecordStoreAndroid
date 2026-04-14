package ca.hccis.buspass.bo

import android.Manifest
import android.annotation.SuppressLint
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Build.*
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import ca.hccis.buspass.MainActivity
import ca.hccis.buspass.R
import ca.hccis.buspass.utility.CisUtility

class NotificationBO(var theMainActivity: MainActivity) {

    // Creates a notification channel.
    public fun createNotificationChannel() {


        // Request notification permission for Android 13+
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(theMainActivity, Manifest.permission.POST_NOTIFICATIONS)
                != PackageManager.PERMISSION_GRANTED
            ) {
                ActivityCompat.requestPermissions(
                    theMainActivity,
                    arrayOf(Manifest.permission.POST_NOTIFICATIONS),
                    1 // Request code
                )
            }
        }


        val channelId = "bus_pass_notifications" // Unique ID for the notification channel.
        val name = "Bus Pass Updates" // Name of the channel displayed to the user.
        val descriptionText = "Notifications for new bus passes" // Description for the channel.
        val importance = NotificationManager.IMPORTANCE_HIGH // Importance level of the channel.
        val channel = NotificationChannel(channelId, name, importance).apply {
            description = descriptionText
        }

        // Registers the channel with the system notification manager.
        val notificationManager: NotificationManager =
            theMainActivity.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createNotificationChannel(channel)
    }

    // Sends a local notification to the user.
    @SuppressLint("NotificationPermission")
    public fun sendNotification(title: String, body: String, channelId: String) {

        // Creates an intent to open MainActivity when the notification is clicked.
        val intent = Intent(theMainActivity, MainActivity::class.java)
        val pendingIntent: PendingIntent = PendingIntent.getActivity(
            theMainActivity,
            0,
            intent,
            PendingIntent.FLAG_IMMUTABLE // Ensures the pending intent cannot be modified by other apps.
        )

        val notificationId = 1 // Unique ID for the notification.

        // Gets the system notification manager to manage notifications.
        val notificationManager =
            theMainActivity.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        // Creates the notification using the helper method.
        val notification = createNotification(channelId, pendingIntent, title, body)

        // Delays sending the notification to ensure the UI is fully initialized.
        theMainActivity.window.decorView.postDelayed({
            notificationManager.notify(notificationId, notification)
        }, 1000)
    }

    // Helper method to create a notification object.
    private fun createNotification(channelId: String, pendingIntent: PendingIntent, title: String, body: String): Notification {
        return NotificationCompat.Builder(theMainActivity, channelId)
            .setSmallIcon(R.drawable.baseline_bus_alert_24) // Icon displayed in the notification.
            .setContentTitle(title) // Title of the notification.
            .setContentText(body) // Body text of the notification.
            .setPriority(NotificationCompat.PRIORITY_DEFAULT) // Priority level for the notification.
            .setChannelId(channelId) // Channel ID for associating the notification with a channel.
            .setContentIntent(pendingIntent) // Intent triggered when the notification is clicked.
            .setAutoCancel(true) // Automatically dismisses the notification when clicked.
            .build()
    }


}