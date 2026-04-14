package ca.hccis.buspass.bo

import android.content.Context
import android.util.Log
import ca.hccis.buspass.contentprovider.CalendarProviderHelper
import ca.hccis.buspass.entity.BusPass
import ca.hccis.buspass.utility.CisUtility
import java.math.BigDecimal
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

object BusPassBO {
    /**
     * Calculate the cost of the bus pass and set it's value in the bus pass object.
     *
     * $1 per day for the first 20 days
     * $0.50 for each day over 20 days
     *
     * $10 premium if to include rural routes
     *
     * Adjustments based on type
     * 3-K12 are free
     * 4-Seniors get a 25% discount on their subtotal
     * 5-Students get a 20% discount on their subtotal
     *
     * @param busPass
     * @return the cost
     * @author BJM
     * @since 20241025
     */
    fun calculateBusPassCost(busPass: BusPass): Double {
        var cost = (1 * busPass.lengthOfPass).toDouble()

        //adjust cost since days over 20 are 0.5$ less per day
        if (busPass.lengthOfPass > 20) {
            cost -= (busPass.lengthOfPass - 20) * 0.5
        }

        if (busPass.validForRuralRoute) {
            cost += 10.0
        }

        when (busPass.passType) {
            3 -> {}
            4 -> cost = 0.0
            5 -> cost *= (1 - 0.2)
            6 -> cost *= (1 - 0.25)
        }
        busPass.cost = BigDecimal(cost)
        return cost
    }

    /**
     * Set default values
     *
     * @param busPass
     * @author BJM
     * @since 20241025
     */
    fun setBusPassDefaults(busPass: BusPass) {
        busPass.lengthOfPass = 31
        busPass.cost = BigDecimal(0)
        busPass.passType = 3
        busPass.startDate = CisUtility.getCurrentDate("yyyy-MM-dd")
    }

    /**
     * Adds a Google Calendar reminder for the bus pass expiry.
     *
     * This method calculates the expiry date of a bus pass based on its start date and duration (in months),
     * then schedules a reminder in the user's Google Calendar.
     *
     * @param context The application context used to access system services.
     * @param busPass An instance of `BusPass` containing details such as start date, duration, and route.
     */
    fun addBusPassReminder(context: Context, busPass: BusPass) {
        // List of date formats to try while parsing the start date.
        val dateFormats = listOf("dd/MM/yyyy", "MM/dd/yyyy", "yyyy-MM-dd")
        var startDateMillis: Long? = null  // Variable to store the parsed start date in milliseconds.
        var eventMillis: Long =  0
        // Try parsing the start date using different formats to ensure flexibility.
        for (format in dateFormats) {
            try {
                val dateFormat = SimpleDateFormat(format, Locale.getDefault()) // Create a date formatter for the current format.
                startDateMillis = dateFormat.parse(busPass.startDate)?.time // Attempt to parse the date.

                if (startDateMillis != null) {
                    // Calculate the bus pass expiry date by adding the duration in months to the start date.
                    eventMillis = startDateMillis + (busPass.lengthOfPass*86400000) //Add based on length of pass
                    break
                } // Exit loop if parsing is successful.
            } catch (e: ParseException) {
                Log.e("BusPassBO", "Date parsing failed for format: $format with date: ${busPass.startDate}")
            }
        }

        // If all parsing attempts fail, log an error and stop execution.
        if (startDateMillis == null) {
            Log.e("BusPassBO", "Failed to parse start date: ${busPass.startDate}. Reminder will not be added.")
            return
        }

        val calendar = Calendar.getInstance().apply {
            timeInMillis = eventMillis // Set the calendar to the parsed start date.
        }
        val endDateMillis = calendar.timeInMillis // Store the calculated expiry date in milliseconds.

        // Define the event title and description.
        val title = "Bus Pass Expiry Reminder"
        val description = "Your bus pass for ${busPass.preferredRoute} will expire after ${busPass.lengthOfPass} days."

        // Log details of the reminder being added.
        Log.d("BusPassBO", "Adding reminder: $title (Start: $startDateMillis, End: $endDateMillis)")

        // Call the CalendarProviderHelper method to insert the event into Google Calendar.
        CalendarProviderHelper.addEventToCalendar(
            context,
            title,
            description,
            eventMillis,
            endDateMillis,
            busPass.lengthOfPass // Pass the duration of the bus pass.
        )
    }

}