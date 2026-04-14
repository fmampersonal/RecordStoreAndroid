package ca.hccis.buspass.contentprovider

import android.content.ContentUris
import android.content.ContentValues
import android.content.Context
import android.provider.CalendarContract
import android.util.Log
import java.util.TimeZone

/**
 * CalendarProviderHelper
 *
 * This helper object provides utility functions to interact with the Google Calendar
 * content provider on Android. It allows adding events, retrieving existing calendars,
 * checking for existing events, and setting reminders.
 *
 * Features:
 * - Add an event to the Google Calendar with recurrence rules based on bus pass duration.
 * - Retrieve the first available Google Calendar ID.
 * - Log existing calendar events.
 * - Add a reminder to a calendar event (default 30 minutes before the event).
 *
 * Requirements:
 * - The app must have `android.permission.WRITE_CALENDAR` and `android.permission.READ_CALENDAR` permissions.
 * - The device should have a Google account with a valid calendar.
 */
object CalendarProviderHelper {

    /**
     * Adds an event to the user's Google Calendar.
     *
     * @param context The application context used to access content resolver.
     * @param title The title of the event.
     * @param description A description of the event.
     * @param startDateMillis The start time of the event in milliseconds.
     * @param endDateMillis The end time of the event in milliseconds.
     * @param lengthOfPass The duration of the bus pass in months, used for recurrence.
     */
    fun addEventToCalendar(
        context: Context,
        title: String,
        description: String,
        startDateMillis: Long,
        endDateMillis: Long,
        lengthOfPass: Int
    ) {
        val calendarId = getCalendarId(context) // Retrieve the primary Google Calendar ID
        if (calendarId == -1L) {
            Log.e("CalendarProvider", "No valid Google Calendar found! Event cannot be added.")
            return
        }

        // Define event details
        val contentValues = ContentValues().apply {
            put(CalendarContract.Events.DTSTART, startDateMillis) // Event start timestamp
            put(CalendarContract.Events.DTEND, endDateMillis) // Event end timestamp
            put(CalendarContract.Events.TITLE, title) // Event title
            put(CalendarContract.Events.DESCRIPTION, description) // Event description
            put(CalendarContract.Events.CALENDAR_ID, calendarId) // Associate event with Google Calendar ID
            put(CalendarContract.Events.EVENT_TIMEZONE, TimeZone.getDefault().id) // Set to device's default timezone
            //put(CalendarContract.Events.RRULE, "FREQ=MONTHLY;COUNT=$lengthOfPass") // Define recurrence rule (monthly for bus pass duration)
        }

        // Insert the event into the calendar
        val uri = context.contentResolver.insert(CalendarContract.Events.CONTENT_URI, contentValues)

        if (uri == null) {
            Log.e("CalendarProvider", "Failed to insert event into Calendar")
        } else {
            val eventId = ContentUris.parseId(uri) // Extract the event ID from URI
            Log.d("CalendarProvider", "Event created successfully! ID: $eventId")
            addReminderToEvent(context, eventId) // Add a reminder for the event
        }
    }

    /**
     * Retrieves the ID of a Google Calendar associated with the device.
     *
     * @param context The application context used to query the content provider.
     * @return The calendar ID if found, otherwise -1.
     */
    fun getCalendarId(context: Context): Long {
        val projection = arrayOf(
            CalendarContract.Calendars._ID,
            CalendarContract.Calendars.ACCOUNT_NAME,
            CalendarContract.Calendars.ACCOUNT_TYPE
        )
        val uri = CalendarContract.Calendars.CONTENT_URI
        val cursor = context.contentResolver.query(uri, projection, null, null, null)

        var selectedCalendarId: Long = -1

        cursor?.use {
            while (it.moveToNext()) {
                val id = it.getLong(0) // Calendar ID
                val accountType = it.getString(2) // Account type (Google, local, etc.)

                // Prefer Google Calendar
                if (accountType == "com.google") {
                    selectedCalendarId = id
                }
            }
        }

        if (selectedCalendarId == -1L) {
            Log.e("CalendarProvider", "No Google Calendar found!")
        }
        return selectedCalendarId
    }

    /**
     * Logs existing events in the Google Calendar.
     *
     * @param context The application context used to query the content provider.
     */
    fun checkExistingEvents(context: Context) {
        val projection = arrayOf(
            CalendarContract.Events._ID,
            CalendarContract.Events.TITLE,
            CalendarContract.Events.DTSTART
        )
        val cursor = context.contentResolver.query(CalendarContract.Events.CONTENT_URI, projection, null, null, null)

        cursor?.use {
            while (it.moveToNext()) {
                val eventId = it.getLong(0) // Event ID
                val eventTitle = it.getString(1) // Event Title
                val eventStart = it.getLong(2) // Event Start Date
                Log.d("CalendarProvider", "Event Found: ID=$eventId, Title=$eventTitle, Start=$eventStart")
            }
        }
    }

    /**
     * Adds a reminder to an existing event in Google Calendar.
     *
     * The reminder is set to notify the user 30 minutes before the event.
     *
     * @param context The application context used to access the content provider.
     * @param eventId The ID of the event to which the reminder will be added.
     */
    fun addReminderToEvent(context: Context, eventId: Long) {
        if (eventId <= 0) {
            Log.e("CalendarProvider", "Invalid eventId: $eventId - Cannot add reminder")
            return
        }

        val values = ContentValues().apply {
            put(CalendarContract.Reminders.EVENT_ID, eventId) // Link reminder to event
            put(CalendarContract.Reminders.MINUTES, 30) // Notify 30 minutes before event
            put(CalendarContract.Reminders.METHOD, CalendarContract.Reminders.METHOD_ALERT) // Use alert notification
        }

        try {
            val uri = context.contentResolver.insert(CalendarContract.Reminders.CONTENT_URI, values)
            if (uri == null) {
                Log.e("CalendarProvider", "Failed to insert reminder")
            } else {
                Log.d("CalendarProvider", "Reminder added successfully")
            }
        } catch (e: Exception) {
            Log.e("CalendarProvider", "Error inserting reminder: ${e.message}", e)
        }
    }
}
