package com.example.recordstore.contentprovider

import android.content.ContentUris
import android.content.ContentValues
import android.content.Context
import android.provider.CalendarContract
import android.util.Log
import java.util.TimeZone

object CalendarProviderHelper {

    fun addEventToCalendar(
        context: Context,
        title: String,
        description: String,
        startDateMillis: Long,
        endDateMillis: Long
    ) {
        val calendarId = getCalendarId(context)
        if (calendarId == -1L) {
            Log.e("CalendarProvider", "No valid Google Calendar found!")
            return
        }

        val contentValues = ContentValues().apply {
            put(CalendarContract.Events.DTSTART, startDateMillis)
            put(CalendarContract.Events.DTEND, endDateMillis)
            put(CalendarContract.Events.TITLE, title)
            put(CalendarContract.Events.DESCRIPTION, description)
            put(CalendarContract.Events.CALENDAR_ID, calendarId)
            put(CalendarContract.Events.EVENT_TIMEZONE, TimeZone.getDefault().id)
        }

        try {
            val uri = context.contentResolver.insert(CalendarContract.Events.CONTENT_URI, contentValues)
            if (uri != null) {
                val eventId = ContentUris.parseId(uri)
                Log.d("CalendarProvider", "Event created! ID: $eventId")
                addReminderToEvent(context, eventId)
            }
        } catch (e: SecurityException) {
            Log.e("CalendarProvider", "Missing Calendar Permissions!", e)
        }
    }

    fun getCalendarId(context: Context): Long {
        val projection = arrayOf(
            CalendarContract.Calendars._ID,
            CalendarContract.Calendars.ACCOUNT_NAME,
            CalendarContract.Calendars.ACCOUNT_TYPE
        )
        try {
            val cursor = context.contentResolver.query(CalendarContract.Calendars.CONTENT_URI, projection, null, null, null)
            var selectedCalendarId: Long = -1

            cursor?.use {
                while (it.moveToNext()) {
                    val id = it.getLong(0)
                    val accountType = it.getString(2)
                    if (accountType == "com.google") {
                        selectedCalendarId = id
                    }
                }
            }
            return if (selectedCalendarId != -1L) selectedCalendarId else 1L // Fallback to default local calendar
        } catch (e: SecurityException) {
            return -1L
        }
    }

    fun addReminderToEvent(context: Context, eventId: Long) {
        val values = ContentValues().apply {
            put(CalendarContract.Reminders.EVENT_ID, eventId)
            put(CalendarContract.Reminders.MINUTES, 30)
            put(CalendarContract.Reminders.METHOD, CalendarContract.Reminders.METHOD_ALERT)
        }
        try {
            context.contentResolver.insert(CalendarContract.Reminders.CONTENT_URI, values)
        } catch (e: SecurityException) {
            Log.e("CalendarProvider", "Missing Calendar Permissions!", e)
        }
    }
}